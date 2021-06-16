package com.data.repositories.movie

import com.data.Response
import com.data.models.Movie

interface MovieRepository {
    fun getMovie(cBack: (Response<Movie>) -> Unit)
    fun getMoviesByGuessed(cBack: (Response<List<Movie>>) -> Unit)
    fun updateMovieGuessed(movie: Movie)
}
