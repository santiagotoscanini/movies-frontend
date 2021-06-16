package com.data.callbacks

import com.data.ErrorType
import com.data.models.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.data.Response.Success
import com.data.Response.Error
import com.data.commons.INTERNAL_SERVER_ERROR
import com.data.commons.STATUS_OK

class GetMovieCallback(private val cBack: (com.data.Response<Movie>) -> Unit): Callback<Movie> {

    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
        when(response.code()){
            STATUS_OK -> {
                response.body()?.let {
                    cBack(Success(it))
                }
            }
            INTERNAL_SERVER_ERROR -> cBack(Error(ErrorType.ErrorMovieServerProblem))
        }
    }

    override fun onFailure(call: Call<Movie>, t: Throwable) {
        cBack(Error(ErrorType.ErrorWithTheAPIRequest))
    }
}
