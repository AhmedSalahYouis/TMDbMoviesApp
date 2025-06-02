package com.asalah.data.entities

import com.asalah.domain.entities.MovieEntity
import com.google.gson.annotations.SerializedName

data class MovieDataResponse(
    @SerializedName("page") val page: Int?,
    @SerializedName("results") val moviesList: List<MovieData>?,
    @SerializedName("total_pages") val totalPages: Int?,
    @SerializedName("total_results") val totalResults: Int?
)

data class MovieData(
    @SerializedName("id") val id: Int?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("genres") val genres: List<GenreData>?,
    @SerializedName("poster_path") val image: String?,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("original_title") val title: String?,
    @SerializedName("release_date") val releaseDate: String?,
)

fun MovieData.toDomain() = MovieEntity(
    id = id ?: 0,
    image = image ?: "",
    overview = overview ?: "",
    title = title ?: "",
    runtime = runtime ?: 0,
    releaseDate = releaseDate ?: "",
    genres = genres?.map { it.toDomain() } ?: emptyList(),
    isFavorite = false
)