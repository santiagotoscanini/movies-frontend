package com.data.repositories.movie

import com.data.Response
import com.data.callbacks.GetAnyCallback
import com.data.callbacks.GetMovieCallback
import com.data.callbacks.GetRankedMoviesCallback
import com.data.commons.COUNT_OF_MOVIES
import com.data.commons.PAGE_SIZE
import com.data.commons.SORT_ORDER
import com.data.datasources.MovieDataSource
import com.data.models.Movie
import com.data.models.SortOrder
import com.data.models.UpdateMovieData

class MovieRepositoryRetrofit(private val movieDataSource: MovieDataSource) : MovieRepository {

    override fun getMovie(cBack: (Response<Movie>) -> Unit) {
        movieDataSource.getMovie().enqueue(GetMovieCallback(cBack))
    }

    override fun getMoviesByGuessed(cBack: (Response<List<Movie>>) -> Unit) {
        movieDataSource.getMoviesRanked(
            mapOf(
                PAGE_SIZE to COUNT_OF_MOVIES,
                SORT_ORDER to SortOrder.Guessed.name
            )
        ).enqueue(GetRankedMoviesCallback(cBack))
    }

    override fun updateMovieGuessed(movie: Movie) {
        movieDataSource.updateMovieGuessed(
            movie.movieId,
            UpdateMovieData(1, 0)
        ).enqueue(GetAnyCallback())
    }

}
