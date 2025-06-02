package com.example.tmdbmoviesapp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.asalah.domain.usecase.AddMovieToFavoriteUseCase
import com.asalah.domain.usecase.IsMovieFavoriteUseCase
import com.asalah.domain.usecase.GetFavoriteMoviesUseCase
import com.asalah.domain.usecase.RemoveMovieFromFavoriteUseCase
import com.asalah.domain.util.onSuccess
import com.example.tmdbmoviesapp.model.MovieUiModel
import com.example.tmdbmoviesapp.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteMovies: GetFavoriteMoviesUseCase,
    private val isMovieFavorite: IsMovieFavoriteUseCase,
    private val addMovieToFavorite: AddMovieToFavoriteUseCase,
    private val removeMovieFromFavorite: RemoveMovieFromFavoriteUseCase,
) : ViewModel() {

    val favorites: Flow<PagingData<MovieUiModel>> = getFavoriteMovies(20).map {
        it.map { it.toUiModel(true) }
    }.cachedIn(viewModelScope)

    // Intent handler
    fun processIntent(intent: FavoritesIntent) {
        when (intent) {
            is FavoritesIntent.ToggleFavorite -> handleFavoriteMovie(intent.movie)
        }
    }

    private fun handleFavoriteMovie(movie: MovieUiModel) {
        viewModelScope.launch {
            isMovieFavorite.invoke(movie.id).onSuccess { isFav ->
                if (isFav) {
                    removeMovieFromFavorite(movie.id)
                } else {
                    addMovieToFavorite(movie.id)
                }
            }
        }
    }
}
