package com.asalah.domain.usecase

import com.asalah.domain.entities.MovieEntity
import com.asalah.domain.repository.IMovieRepository
import com.asalah.domain.util.Result

class GetMovieDetailsUseCase(
    private val movieRepository: IMovieRepository
) {
    suspend operator fun invoke(movieId: Int): Result<MovieEntity> = movieRepository.getMovie(movieId)
}
