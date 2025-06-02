package com.asalah.data.db.movies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.asalah.data.entities.MovieRemoteKeyDbData

@Dao
interface MovieRemoteKeyDao {

    @Query("SELECT * FROM movies_remote_keys WHERE id=:id")
    suspend fun getRemoteKeyByMovieId(id: Int): MovieRemoteKeyDbData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(keys: MovieRemoteKeyDbData)
    
    @Query("DELETE FROM movies_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM movies_remote_keys WHERE id = (SELECT MAX(id) FROM movies_remote_keys)")
    suspend fun getLastRemoteKey(): MovieRemoteKeyDbData?
}