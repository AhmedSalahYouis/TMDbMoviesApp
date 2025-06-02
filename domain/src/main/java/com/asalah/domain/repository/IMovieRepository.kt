package com.asalah.domain.repository

import androidx.paging.PagingData
import com.asalah.domain.entities.MovieEntity
import com.asalah.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    fun movies(pageSize: Int): Flow<PagingData<MovieEntity>>
    fun favoriteMovies(pageSize: Int): Flow<PagingData<MovieEntity>>
    suspend fun getMovie(movieId: Int): Result<MovieEntity>
    suspend fun isFavoriteMovie(movieId: Int): Result<Boolean>
    suspend fun addMovieToFavorite(movieId: Int)
    suspend fun removeMovieFromFavorite(movieId: Int)
}