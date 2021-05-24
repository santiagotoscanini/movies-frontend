package com.data

sealed class Response<out R> {

    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val error: ErrorType) : Response<Nothing>()

    object Loading : Response<Nothing>()
}

sealed class ErrorType {
    // here we gonna put 'object ErrorX : ErrorType()'
}
