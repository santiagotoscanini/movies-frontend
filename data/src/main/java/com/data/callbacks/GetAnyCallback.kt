package com.data.callbacks

import com.data.ErrorType
import com.data.commons.INTERNAL_SERVER_ERROR
import com.data.commons.STATUS_OK
import com.data.models.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetAnyCallback(): Callback<Any> {

    override fun onResponse(call: Call<Any>, response: Response<Any>) {
    }

    override fun onFailure(call: Call<Any>, t: Throwable) {
    }
}