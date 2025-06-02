package com.asalah.domain.usecase

import androidx.paging.PagingData
import com.asalah.domain.entities.MovieEntity
import com.asalah.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val movieRepository: IMovieRepository,
) {
    fun movies(pageSize: Int): Flow<PagingData<MovieEntity>> = movieRepository.movies(pageSize)
}
