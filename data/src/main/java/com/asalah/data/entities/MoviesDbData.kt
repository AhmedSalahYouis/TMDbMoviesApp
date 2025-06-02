package com.asalah.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.asalah.domain.entities.Genre
import com.asalah.domain.entities.MovieEntity

@Entity(tableName = "movies")
data class MoviesDbEntity(
    @PrimaryKey val id: Int,
    val runtime: Int,
    val overview: String,
    val genres: List<Genre>,
    val image: String,
    val title: String,
    val releaseDate: String,
    val isFavorite: Boolean
)

fun MovieEntity.toDbData() = MoviesDbEntity(
    id = id,
    image = image,
    overview = overview,
    runtime = runtime,
    title = title,
    releaseDate = releaseDate,
    genres = genres,
    isFavorite = isFavorite
)

fun MoviesDbEntity.toDomain() = MovieEntity(
    id = id,
    image = image,
    overview = overview,
    runtime = runtime,
    genres = genres,
    title = title,
    releaseDate = releaseDate,
    isFavorite = isFavorite
)