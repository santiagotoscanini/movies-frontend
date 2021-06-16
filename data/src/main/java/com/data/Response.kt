package com.data

sealed class Response<out R> {

    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val error: ErrorType) : Response<Nothing>()
}

sealed class ErrorType {
    object ErrorGameNotFound: ErrorType()
    object ErrorTeamNotFound: ErrorType()
    object ErrorNoTeammatesFound: ErrorType()
    object ErrorWithTheAPIRequest: ErrorType()
    object ErrorMovieServerProblem: ErrorType()
    object ErrorBadRequestGettingRanking: ErrorType()
}
