package com.asalah.data.api

import com.asalah.data.BuildConfig
import com.asalah.data.entities.MovieData
import com.asalah.data.entities.MovieDataResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("/3/movie/popular")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): MovieDataResponse

    @GET("3/movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String = BuildConfig.API_KEY): MovieData
}