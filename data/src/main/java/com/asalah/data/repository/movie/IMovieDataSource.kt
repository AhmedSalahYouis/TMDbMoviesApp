package com.asalah.data.repository.movie

import androidx.paging.PagingSource
import com.asalah.data.entities.MoviesDbEntity
import com.asalah.data.entities.MovieRemoteKeyDbData
import com.asalah.domain.entities.MovieEntity
import com.asalah.domain.util.Result

interface IMovieDataSource {

    interface Remote {
        suspend fun fetchMovies(page: Int, limit: Int): Result<List<MovieEntity>>
        suspend fun fetchMovieById(movieId: Int): Result<MovieEntity>
    }

    interface Local {
        fun movies(): PagingSource<Int, MoviesDbEntity>
        suspend fun getMovies(): Result<List<MovieEntity>>
        suspend fun getMovieById(movieId: Int): Result<MovieEntity>
        suspend fun insertMovies(movieEntities: List<MovieEntity>)
        suspend fun insertRemoteKey(key: MovieRemoteKeyDbData)
        suspend fun getLastRemoteKey(): MovieRemoteKeyDbData?
        suspend fun clearMovies()
        suspend fun clearRemoteKeys()
    }
}