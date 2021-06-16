package com.data.models

data class Team(
    val id: String,
    val teammatesNames: List<String> = listOf(),
    var image: String? = null,
    var nextTeammateTurn: Int = 0
){
    companion object TeamIdentifier{
        const val firstTeam = "FirstTeam"
        const val secondTeam = "SecondTeam"
        const val thirdTeam = "ThirdTeam"
    }
}
