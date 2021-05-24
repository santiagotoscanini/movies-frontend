package com.data.models

data class Team(
    val id: String,
    val teammatesNames: List<String> = listOf(),
    var image: String? = null
){
    companion object TeamIdentifier{
        const val firstTeam = "FirstTeam"
        const val secondTeam = "SecondTeam"
        const val thirdTeam = "ThirdTeam"
    }
}
