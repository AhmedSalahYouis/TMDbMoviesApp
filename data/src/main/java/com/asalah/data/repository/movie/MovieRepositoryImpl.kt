package com.asalah.data.repository.movie

import androidx.paging.*
import com.asalah.data.entities.toDomain
import com.asalah.data.repository.favorite.FavoriteMoviesDataSource
import com.asalah.domain.entities.MovieEntity
import com.asalah.domain.repository.IMovieRepository
import com.asalah.domain.util.Result
import com.asalah.domain.util.getResult
import com.asalah.domain.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class MovieRepositoryImpl(
    private val remote: IMovieDataSource.Remote,
    private val local: IMovieDataSource.Local,
    private val remoteMediator: MovieRemoteMediator,
    private val localFavorite: FavoriteMoviesDataSource.Local
) : IMovieRepository {

    @OptIn(ExperimentalPagingApi::class)
        override fun movies(pageSize: Int): Flow<PagingData<MovieEntity>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false,
        ),
        remoteMediator = remoteMediator,
        pagingSourceFactory = { local.movies() }
    ).flow.map { pagingData ->
        pagingData.map { it.toDomain() }
    }

    override fun favoriteMovies(pageSize: Int): Flow<PagingData<MovieEntity>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        ), pagingSourceFactory = { localFavorite.favoriteMovies() }
    ).flow.map { pagingData ->
        pagingData.map { it.toDomain() }
    }

    override suspend fun getMovie(movieId: Int): Result<MovieEntity> = local.getMovieById(movieId).getResult({
        it
    }, {
        remote.fetchMovieById(movieId)
    })

    override suspend fun isFavoriteMovie(movieId: Int): Result<Boolean> = localFavorite.isMovieFavorite(movieId)

    override suspend fun addMovieToFavorite(movieId: Int) {
        getMovie(movieId).onSuccess {
            local.insertMovies(Collections.singletonList(it)) // creates a List<Movie> from a single Movie
            localFavorite.addMovieToFavorite(movieId)
        }
    }

    override suspend fun removeMovieFromFavorite(movieId: Int) = localFavorite.removeMovieFromFavorite(movieId)


}