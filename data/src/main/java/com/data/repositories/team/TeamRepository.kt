package com.data.repositories.team

import com.data.Response
import com.data.models.Game
import com.data.models.Team

interface TeamRepository {
    fun getTeam(teamId: String, cBack: (Response<Team>) -> Unit)
    fun saveTeam(team: Team)
    fun getTeams(cBack: (Response<List<Team>>) -> Unit)
    fun createOrUpdateGame(game: Game)
    fun cleanTeam()
    fun cleanGame()
    fun getGame(cBack: (Response<Game>) -> Unit)
    fun getNextTeamTurn(cBack: (Response<String>) -> Unit)
    fun getNextTeammateTurn(teamId: String, cBack: (Response<String>) -> Unit)
}
