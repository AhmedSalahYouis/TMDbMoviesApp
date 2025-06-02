package com.example.tmdbmoviesapp.model

data class MovieDetailsUiModel(
    val id: Int,
    val title: String,
    val overview: String,
    val genres: List<GenreUiModel>,
    val runtime: Int,
    val imageUrl: String
)


