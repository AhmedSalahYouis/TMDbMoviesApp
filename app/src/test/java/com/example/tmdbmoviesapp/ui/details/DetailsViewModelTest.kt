// File: DetailsViewModelTest.kt

package com.example.tmdbmoviesapp.ui.details

import app.cash.turbine.test
import com.asalah.domain.entities.MovieEntity
import com.asalah.domain.usecase.GetMovieDetailsUseCase
import com.asalah.domain.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsViewModelTest {

    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        getMovieDetailsUseCase = mockk()
        viewModel = DetailsViewModel(getMovieDetailsUseCase)
    }

    @Test
    fun `processIntent FetchMovieDetails - success state`() = runTest {
        // Arrange
        val movieId = 1
        val movieEntity = MovieEntity(
            id = movieId,
            title = "Test Movie",
            overview = "Test Overview",
            genres = emptyList(),
            runtime = 120,
            releaseDate = "2023-01-01",
            image = "poster",
            isFavorite = false
        )
        coEvery { getMovieDetailsUseCase(movieId) } returns Result.Success(movieEntity)

        // Act
        viewModel.processIntent(MovieDetailsIntent.FetchMovieDetails(movieId))

        // Assert
        viewModel.state.test {
            assertEquals(DetailsState.Loading, awaitItem())
            val successState = awaitItem()
            assertTrue(successState is DetailsState.Success)
            assertEquals("Test Movie", (successState as DetailsState.Success).details.title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `processIntent FetchMovieDetails - error state`() = runTest {
        // Arrange
        val movieId = 1
        val exception = RuntimeException("Failed to load movie")
        coEvery { getMovieDetailsUseCase(movieId) } returns Result.Error(exception)

        // Act
        viewModel.processIntent(MovieDetailsIntent.FetchMovieDetails(movieId))

        // Assert
        viewModel.state.test {
            val errorState = awaitItem()
            assertTrue(errorState is DetailsState.Error)
            assertTrue((errorState as DetailsState.Error).message.contains("Failed to load movie"))
            cancelAndIgnoreRemainingEvents()
        }
    }
}
