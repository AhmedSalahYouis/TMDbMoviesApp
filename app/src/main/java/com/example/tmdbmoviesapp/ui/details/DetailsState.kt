package com.example.tmdbmoviesapp.ui.details

import com.example.tmdbmoviesapp.model.MovieDetailsUiModel

sealed class DetailsState {
    data object Loading : DetailsState()
    data class Success(val details: MovieDetailsUiModel) : DetailsState()
    data class Error(val message: String) : DetailsState()
}