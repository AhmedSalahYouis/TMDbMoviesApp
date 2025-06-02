package com.asalah.data.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.asalah.data.entities.MoviesDbEntity
import com.asalah.data.repository.favorite.FavoriteMoviesDataSource
import com.asalah.data.repository.movie.IMovieDataSource
import com.asalah.data.repository.movie.MovieRemoteMediator
import com.asalah.data.repository.movie.MovieRepositoryImpl
import com.asalah.domain.entities.MovieEntity
import com.asalah.domain.util.Result
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MovieRepositoryImplTest {

    private lateinit var remote: IMovieDataSource.Remote
    private lateinit var local: IMovieDataSource.Local
    private lateinit var localFavorite: FavoriteMoviesDataSource.Local
    private lateinit var remoteMediator: MovieRemoteMediator
    private lateinit var repository: MovieRepositoryImpl

    @BeforeEach
    fun setUp() {
        remote = mockk()
        local = mockk()
        localFavorite = mockk()
        remoteMediator = mockk()
        repository = MovieRepositoryImpl(remote, local, remoteMediator, localFavorite)
    }

    @Test
    fun `movies() returns paged data from local data source`() = runBlocking {
        val movieDb = MoviesDbEntity(
            id = 1,
            runtime = 120,
            overview = "Test Movie Overview",
            genres = emptyList(),
            image = "poster",
            title = "Test Movie",
            releaseDate = "2024-06-01",
            isFavorite = false
        )
        val pagingSource = object : PagingSource<Int, MoviesDbEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesDbEntity> {
                return LoadResult.Page(listOf(movieDb), prevKey = null, nextKey = null)
            }
            override fun getRefreshKey(state: PagingState<Int, MoviesDbEntity>): Int {
                return 1
            }
        }
        every { local.movies() } returns pagingSource

        val flow = repository.movies(20)
        val result = flow.first()

        assertTrue(result is PagingData<*>)
    }

    @Test
    fun `favoriteMovies() returns paged data from local favorite data source`() = runBlocking {
        val movieDbEntity = MoviesDbEntity(
            id = 1,
            runtime = 120,
            overview = "Test Movie Overview",
            genres = emptyList(),
            image = "poster",
            title = "Test Movie",
            releaseDate = "2024-06-01",
            isFavorite = false
        )

        val pagingSource = object : PagingSource<Int, MoviesDbEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MoviesDbEntity> {
                return LoadResult.Page(
                    data = listOf(movieDbEntity),
                    prevKey = null,
                    nextKey = null
                )
            }

            override fun getRefreshKey(state: PagingState<Int, MoviesDbEntity>): Int? = null
        }

        every { localFavorite.favoriteMovies() } returns pagingSource

        val flow = repository.favoriteMovies(20)
        val result = flow.first()

        Assertions.assertTrue(result is PagingData<MovieEntity>)
    }

    @Test
    fun `getMovie() returns success from local`() = runBlocking {
        val movie = MovieEntity(1, "Test Movie", "2024-06-01", 0,
            emptyList(),
            "poster", "2025-04-09", false)
        coEvery { local.getMovieById(1) } returns  Result.Success(movie)

        val result = repository.getMovie(1)

        assertTrue(result is Result.Success)
        assertEquals(movie, (result as Result.Success).data)
    }

    @Test
    fun `isFavoriteMovie() returns result from localFavorite`() = runBlocking {
        coEvery { localFavorite.isMovieFavorite(1) } returns Result.Success(true)

        val result = repository.isFavoriteMovie(1)

        assertTrue(result is Result.Success)
        assertEquals(true, (result as Result.Success).data)
    }

    @Test
    fun `addMovieToFavorite() fetches movie and adds to favorite`() = runBlocking {
        val movie = MovieEntity(1, "Test Movie", "2024-06-01", 0,
            emptyList(),
            "poster", "2025-04-09", false)
        coEvery { local.getMovieById(1) } returns Result.Success(movie)
        coEvery { local.insertMovies(any()) } just Runs
        coEvery { localFavorite.addMovieToFavorite(1) } just Runs

        repository.addMovieToFavorite(1)

        coVerify { local.getMovieById(1) }
        coVerify { local.insertMovies(match { it.contains(movie) }) }
        coVerify { localFavorite.addMovieToFavorite(1) }
    }

    @Test
    fun `removeMovieFromFavorite() removes from favorite`() = runBlocking {
        coEvery { localFavorite.removeMovieFromFavorite(1) } just Runs

        repository.removeMovieFromFavorite(1)

        coVerify { localFavorite.removeMovieFromFavorite(1) }
    }
}
