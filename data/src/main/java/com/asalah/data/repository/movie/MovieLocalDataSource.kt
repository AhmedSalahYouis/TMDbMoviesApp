package com.asalah.data.repository.movie

import androidx.paging.PagingSource
import com.asalah.data.db.movies.MovieDao
import com.asalah.data.db.movies.MovieRemoteKeyDao
import com.asalah.data.entities.MoviesDbEntity
import com.asalah.data.entities.MovieRemoteKeyDbData
import com.asalah.data.entities.toDbData
import com.asalah.data.entities.toDomain
import com.asalah.data.util.exception.NoAvailableDataException
import com.asalah.data.util.DiskExecutor
import com.asalah.domain.entities.MovieEntity
import com.asalah.domain.util.Result
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext

class MovieLocalDataSource(
    private val executor: DiskExecutor,
    private val movieDao: MovieDao,
    private val remoteKeysDao: MovieRemoteKeyDao,
) : IMovieDataSource.Local {

    override fun movies(): PagingSource<Int, MoviesDbEntity> = movieDao.movies()

    override suspend fun getMovies(): Result<List<MovieEntity>> = withContext(executor.asCoroutineDispatcher()) {
        val movies = movieDao.getMovies()
        return@withContext if (movies.isNotEmpty()) {
            Result.Success(movies.map { it.toDomain() })
        } else {
            Result.Error(NoAvailableDataException())
        }
    }

    override suspend fun getMovieById(movieId: Int): Result<MovieEntity> = withContext(executor.asCoroutineDispatcher()) {
        return@withContext movieDao.getMovie(movieId)?.let {
            Result.Success(it.toDomain())
        } ?: Result.Error(NoAvailableDataException())
    }

    override suspend fun insertMovies(movieEntities: List<MovieEntity>) = withContext(executor.asCoroutineDispatcher()) {
        movieDao.insertMovies(movieEntities.map { it.toDbData() })
    }

    override suspend fun insertRemoteKey(key: MovieRemoteKeyDbData) = withContext(executor.asCoroutineDispatcher()) {
        remoteKeysDao.insertRemoteKey(key)
    }

    override suspend fun getLastRemoteKey(): MovieRemoteKeyDbData? = withContext(executor.asCoroutineDispatcher()) {
        remoteKeysDao.getLastRemoteKey()
    }
    
    override suspend fun clearMovies() = withContext(executor.asCoroutineDispatcher()) {
        movieDao.clearMoviesExceptFavorites()
    }

    override suspend fun clearRemoteKeys() = withContext(executor.asCoroutineDispatcher()) {
        remoteKeysDao.clearRemoteKeys()
    }
}