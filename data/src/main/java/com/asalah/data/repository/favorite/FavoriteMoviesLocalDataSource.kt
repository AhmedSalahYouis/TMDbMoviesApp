package com.asalah.data.repository.favorite

import androidx.paging.PagingSource
import com.asalah.data.db.favoritemovies.FavoriteMovieDao
import com.asalah.data.entities.FavoriteMovieDbData
import com.asalah.data.entities.MoviesDbEntity
import com.asalah.data.util.exception.NoAvailableDataException
import com.asalah.data.util.DiskExecutor
import com.asalah.domain.util.Result
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext

class FavoriteMoviesLocalDataSource(
    private val executor: DiskExecutor,
    private val favoriteMovieDao: FavoriteMovieDao,
) : FavoriteMoviesDataSource.Local {

    override fun favoriteMovies(): PagingSource<Int, MoviesDbEntity> = favoriteMovieDao.favoriteMovies()

    override suspend fun addMovieToFavorite(movieId: Int) = withContext(executor.asCoroutineDispatcher()) {
        favoriteMovieDao.add(FavoriteMovieDbData(movieId))
    }

    override suspend fun removeMovieFromFavorite(movieId: Int) = withContext(executor.asCoroutineDispatcher()) {
        favoriteMovieDao.remove(movieId)
    }

    override suspend fun isMovieFavorite(movieId: Int): Result<Boolean> = withContext(executor.asCoroutineDispatcher()) {
        return@withContext Result.Success(favoriteMovieDao.get(movieId) != null)
    }

    override suspend fun getFavoriteMovieIds(): Result<List<Int>> = withContext(executor.asCoroutineDispatcher()) {
        val movieIds = favoriteMovieDao.getAll().map { it.movieId }
        return@withContext if (movieIds.isNotEmpty()) {
            Result.Success(movieIds)
        } else {
            Result.Error(NoAvailableDataException())
        }
    }

}