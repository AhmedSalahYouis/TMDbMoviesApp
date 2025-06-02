package com.example.tmdbmoviesapp.ui.home

import com.example.tmdbmoviesapp.model.MovieUiModel

sealed class HomeIntent {
    data object FetchMovies : HomeIntent()
    data object RefreshMovies : HomeIntent()
    data class ToggleFavorite(val movie: MovieUiModel) : HomeIntent()
}
