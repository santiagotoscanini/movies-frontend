package com.data.callbacks

import com.data.ErrorType
import com.data.commons.INTERNAL_SERVER_ERROR
import com.data.commons.REQUEST_ERROR
import com.data.commons.STATUS_OK
import com.data.models.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetRankedMoviesCallback(private val cBack: (com.data.Response<List<Movie>>) -> Unit):
    Callback<List<Movie>> {

    override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
        when(response.code()){
            STATUS_OK -> {
                response.body()?.let {
                    cBack(com.data.Response.Success(it))
                }
            }
            REQUEST_ERROR -> cBack(com.data.Response.Error(ErrorType.ErrorBadRequestGettingRanking))
            INTERNAL_SERVER_ERROR -> cBack(com.data.Response.Error(ErrorType.ErrorMovieServerProblem))
        }
    }

    override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
        cBack(com.data.Response.Error(ErrorType.ErrorBadRequestGettingRanking))
    }
}
