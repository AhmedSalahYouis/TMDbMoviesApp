package com.example.tmdbmoviesapp.ui.details

sealed class MovieDetailsIntent {
    data class FetchMovieDetails(val movieId: Int) : MovieDetailsIntent()

}