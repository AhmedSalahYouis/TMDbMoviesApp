package com.asalah.domain.usecase

import androidx.paging.PagingData
import com.asalah.domain.entities.MovieEntity
import com.asalah.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteMoviesUseCase(
    private val movieRepository: IMovieRepository
) {
    operator fun invoke(pageSize: Int): Flow<PagingData<MovieEntity>> = movieRepository.favoriteMovies(pageSize)
}