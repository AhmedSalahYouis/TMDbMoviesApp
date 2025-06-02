package com.example.tmdbmoviesapp.ui.favorites

import com.example.tmdbmoviesapp.model.MovieUiModel

sealed class FavoritesIntent {
    data class ToggleFavorite(val movie: MovieUiModel) : FavoritesIntent()
}
