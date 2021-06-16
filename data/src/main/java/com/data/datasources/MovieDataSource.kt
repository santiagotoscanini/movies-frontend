package com.data.datasources

import com.data.models.Movie
import com.data.models.UpdateMovieData
import retrofit2.Call
import retrofit2.http.*

interface MovieDataSource {

    @GET("random")
    fun getMovie(): Call<Movie>

    @GET(".")
    fun getMoviesRanked(@QueryMap rankedFilters: Map<String, String>): Call<List<Movie>>

    @PUT("{id}")
    fun updateMovieGuessed(@Path("id") movieId: Int, @Body movieUpdateMovieData: UpdateMovieData): Call<Any>
}
