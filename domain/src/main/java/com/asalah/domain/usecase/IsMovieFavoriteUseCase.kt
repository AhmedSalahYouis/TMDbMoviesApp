package com.asalah.domain.usecase

import com.asalah.domain.repository.IMovieRepository
import com.asalah.domain.util.Result

class IsMovieFavoriteUseCase(
    private val movieRepository: IMovieRepository
) {
    suspend operator fun invoke(movieId: Int): Result<Boolean> = movieRepository.isFavoriteMovie(movieId)
}