package com.data.models

data class Game(
    val teamsPositions: HashMap<String, Int?> = hashMapOf(),
    var turnNumber: Int = 0
)
