package com.asalah.domain.entities

data class MovieEntity(
    val id: Int,
    val title: String,
    val overview: String,
    val runtime: Int,
    val genres: List<Genre>,
    val image: String,
    val releaseDate: String,
    var isFavorite: Boolean,
)