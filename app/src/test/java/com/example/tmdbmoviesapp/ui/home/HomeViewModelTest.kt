// HomeViewModelTest.kt
package com.example.tmdbmoviesapp.ui.home

import HomeState
import androidx.paging.PagingData
import app.cash.turbine.test
import com.asalah.domain.entities.MovieEntity
import com.asalah.domain.usecase.AddMovieToFavoriteUseCase
import com.asalah.domain.usecase.GetMoviesUseCase
import com.asalah.domain.usecase.IsMovieFavoriteUseCase
import com.asalah.domain.usecase.RemoveMovieFromFavoriteUseCase
import com.asalah.domain.util.Result
import com.example.tmdbmoviesapp.model.MovieUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    private val getMoviesUseCase: GetMoviesUseCase = mockk(relaxed = true)
    private val isMovieFavorite: IsMovieFavoriteUseCase = mockk(relaxed = true)
    private val addMovieToFavorite: AddMovieToFavoriteUseCase = mockk(relaxed = true)
    private val removeMovieFromFavorite: RemoveMovieFromFavoriteUseCase = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = HomeViewModel(
            getMoviesUseCase,
            isMovieFavorite,
            addMovieToFavorite,
            removeMovieFromFavorite
        )
    }

    @Test
    fun `processIntent FetchMovies updates state with Success`() = runTest {
        // Mock data
        val dummyPagingData = PagingData.from(emptyList<MovieEntity>())
        coEvery { getMoviesUseCase.movies(2) } returns flowOf(dummyPagingData)

        viewModel.state.test {
            viewModel.processIntent(HomeIntent.FetchMovies)

            // Initially Loading
            assertEquals(awaitItem(), HomeState.Loading)
            // Then Success
            val item = awaitItem()
            assertTrue(item is HomeState.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `processIntent ToggleFavorite triggers add or remove useCase`() = runTest {
        val movie = MovieUiModel(1, "Test Movie", 120, "title", "overview","", false )
        coEvery { isMovieFavorite(movie.id) } returns Result.Success(false)

        viewModel.processIntent(HomeIntent.ToggleFavorite(movie))

        coVerify { addMovieToFavorite(movie.id) }
    }
}
