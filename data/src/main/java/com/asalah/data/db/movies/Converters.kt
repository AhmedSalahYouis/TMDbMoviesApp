package com.asalah.data.db.movies

import androidx.room.TypeConverter
import com.asalah.domain.entities.Genre
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromGenreList(genres: List<Genre>): String {
        return json.encodeToString(ListSerializer(Genre.serializer()), genres)
    }

    @TypeConverter
    fun toGenreList(data: String): List<Genre> {
        return json.decodeFromString(ListSerializer(Genre.serializer()), data)
    }
}
