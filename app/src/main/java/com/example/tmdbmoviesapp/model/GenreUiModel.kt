package com.example.tmdbmoviesapp.model

import com.asalah.domain.entities.Genre

data class GenreUiModel(val id: Int, val name: String)

fun Genre.toUiModel() = GenreUiModel(id, name)
