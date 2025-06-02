package com.asalah.data.db.movies

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.asalah.data.entities.MoviesDbEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY id")
    fun movies(): PagingSource<Int, MoviesDbEntity>

    @Query("SELECT * FROM movies ORDER BY id")
    fun getMovies(): List<MoviesDbEntity>

    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovie(movieId: Int): MoviesDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MoviesDbEntity>)

    @Query("DELETE FROM movies WHERE id NOT IN (SELECT movieId FROM favorite_movies)")
    suspend fun clearMoviesExceptFavorites()
}