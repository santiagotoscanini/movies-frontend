package com.data.models

data class Movie(
    val movieName: String,
    val movieId: Int,
    val guessedTimes: Int,
    val selectedTimes: Int,
    val imageURL: String
)
