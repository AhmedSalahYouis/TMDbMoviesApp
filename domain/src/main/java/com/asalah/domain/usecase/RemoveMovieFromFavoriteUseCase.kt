package com.asalah.domain.usecase

import com.asalah.domain.repository.IMovieRepository

class RemoveMovieFromFavoriteUseCase(
    private val movieRepository: IMovieRepository
) {
    suspend operator fun invoke(movieId: Int) = movieRepository.removeMovieFromFavorite(movieId)
}