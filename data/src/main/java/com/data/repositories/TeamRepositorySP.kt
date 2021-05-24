package com.data.repositories

import com.data.Response
import com.data.datasources.GameDataSource
import com.data.models.Team

class TeamRepositorySP(private val gameDataSource: GameDataSource) : TeamRepository {

    override fun getTeam(teamId: String, cBack: (Response<Team>) -> Unit) {
        val team = gameDataSource.getTeams()[teamId]
        if(team == null){
            cBack(Response.Success(Team(id = teamId)))
        }else{
            cBack(Response.Success(team))
        }
    }
}
