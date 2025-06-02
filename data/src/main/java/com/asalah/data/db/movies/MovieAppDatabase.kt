package com.asalah.data.db.movies

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.asalah.data.db.favoritemovies.FavoriteMovieDao
import com.asalah.data.entities.FavoriteMovieDbData
import com.asalah.data.entities.MoviesDbEntity
import com.asalah.data.entities.MovieRemoteKeyDbData

@Database(
    entities = [MoviesDbEntity::class, FavoriteMovieDbData::class, MovieRemoteKeyDbData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieAppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeyDao
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}