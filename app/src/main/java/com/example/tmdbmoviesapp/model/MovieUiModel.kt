package com.example.tmdbmoviesapp.model

import com.asalah.domain.entities.MovieEntity

data class MovieUiModel(
    val id: Int,
    val imageUrl: String,
    val runtime: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    var isFavorite: Boolean = false
)

fun MovieEntity.toUiModel(isFavorite: Boolean) = MovieUiModel(
    id = id,
    imageUrl = image,
    title = title,
    runtime = runtime,
    overview = overview,
    releaseDate = releaseDate,
    isFavorite = isFavorite
)