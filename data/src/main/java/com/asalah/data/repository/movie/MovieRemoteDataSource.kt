package com.asalah.data.repository.movie

import com.asalah.data.api.MovieApi
import com.asalah.data.entities.toDomain
import com.asalah.domain.entities.MovieEntity
import com.asalah.domain.util.Result

class MovieRemoteDataSource(
    private val movieApi: MovieApi
) : IMovieDataSource.Remote {

    override suspend fun fetchMovies(page: Int, limit: Int): Result<List<MovieEntity>> = try {
        val result = movieApi.getMovies(page, limit)
        Result.Success(result.moviesList?.map { it.toDomain() } ?: emptyList())
    } catch (e: Exception) {
        Result.Error(e)
    }

    override suspend fun fetchMovieById(movieId: Int): Result<MovieEntity> = try {
        val result = movieApi.getMovie(movieId)
        Result.Success(result.toDomain())
    } catch (e: Exception) {
        Result.Error(e)
    }
}