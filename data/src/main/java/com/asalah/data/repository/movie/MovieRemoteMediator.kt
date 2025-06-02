package com.asalah.data.repository.movie

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.asalah.data.entities.MoviesDbEntity
import com.asalah.data.entities.MovieRemoteKeyDbData
import com.asalah.domain.util.getResult

private const val MOVIE_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val local: IMovieDataSource.Local,
    private val remote: IMovieDataSource.Remote,
) : RemoteMediator<Int, MoviesDbEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, MoviesDbEntity>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> MOVIE_STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> local.getLastRemoteKey()?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
        }

        remote.fetchMovies(page, state.config.pageSize).getResult({ successResult ->

            if (loadType == LoadType.REFRESH) {
                local.clearMovies()
                local.clearRemoteKeys()
            }

            val movies = successResult.data

            val endOfPaginationReached = movies.isEmpty()

            val prevPage = if (page == MOVIE_STARTING_PAGE_INDEX) null else page - 1
            val nextPage = if (endOfPaginationReached) null else page + 1

            val key = MovieRemoteKeyDbData(prevPage = prevPage, nextPage = nextPage)

            local.insertMovies(movies)
            local.insertRemoteKey(key)

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }, { errorResult ->
            return MediatorResult.Error(errorResult.error)
        })
    }
}