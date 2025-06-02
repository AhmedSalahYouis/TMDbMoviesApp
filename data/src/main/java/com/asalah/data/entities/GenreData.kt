package com.asalah.data.entities

import com.asalah.domain.entities.Genre
import com.google.gson.annotations.SerializedName

data class GenreData(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?
) {
    fun toDomain() = Genre(id ?: 0, name ?: "")
}