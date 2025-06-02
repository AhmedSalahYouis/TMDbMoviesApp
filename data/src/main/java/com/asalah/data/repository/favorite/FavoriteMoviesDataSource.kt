package com.asalah.data.repository.favorite

import androidx.paging.PagingSource
import com.asalah.data.entities.MoviesDbEntity
import com.asalah.domain.util.Result

interface FavoriteMoviesDataSource {

    interface Local {
        fun favoriteMovies(): PagingSource<Int, MoviesDbEntity>
        suspend fun getFavoriteMovieIds(): Result<List<Int>>
        suspend fun addMovieToFavorite(movieId: Int)
        suspend fun removeMovieFromFavorite(movieId: Int)
        suspend fun isMovieFavorite(movieId: Int): Result<Boolean>
    }

}