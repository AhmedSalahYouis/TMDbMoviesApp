package com.asalah.data.module

import android.content.Context
import androidx.room.Room
import com.asalah.data.db.favoritemovies.FavoriteMovieDao
import com.asalah.data.db.movies.MovieDao
import com.asalah.data.db.movies.MovieAppDatabase
import com.asalah.data.db.movies.MovieRemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieAppDatabase {
        return Room.databaseBuilder(context, MovieAppDatabase::class.java, "movies.db").build()
    }

    @Provides
    fun provideMovieDao(movieDatabase: MovieAppDatabase): MovieDao {
        return movieDatabase.movieDao()
    }

    @Provides
    fun provideMovieRemoteKeyDao(movieDatabase: MovieAppDatabase): MovieRemoteKeyDao {
        return movieDatabase.movieRemoteKeysDao()
    }

    @Provides
    fun provideFavoriteMovieDao(movieDatabase: MovieAppDatabase): FavoriteMovieDao {
        return movieDatabase.favoriteMovieDao()
    }
}