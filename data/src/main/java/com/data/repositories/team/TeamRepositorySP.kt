package com.data.repositories.team

import com.data.ErrorType
import com.data.Response
import com.data.datasources.GameDataSource
import com.data.models.Game
import com.data.models.Team

class TeamRepositorySP(private val gameDataSource: GameDataSource) : TeamRepository {

    override fun getTeam(teamId: String, cBack: (Response<Team>) -> Unit) {
        val team = gameDataSource.getTeams()[teamId]
        if (team == null) {
            cBack(Response.Success(Team(id = teamId)))
        } else {
            cBack(Response.Success(team))
        }
    }

    override fun saveTeam(team: Team) {
        gameDataSource.addOrUpdateTeam(team)
    }

    override fun getTeams(cBack: (Response<List<Team>>) -> Unit) {
        val teams = gameDataSource.getTeams().values.toList()
        cBack(Response.Success(teams))
    }

    override fun createOrUpdateGame(game: Game) {
        gameDataSource.saveGame(game)
    }

    override fun cleanTeam() {
        gameDataSource.cleanTeam()
    }

    override fun cleanGame() {
        gameDataSource.cleanGame()
    }

    override fun getGame(cBack: (Response<Game>) -> Unit) {
        val game = gameDataSource.getGame()
        if (game == null) {
            cBack(Response.Error(ErrorType.ErrorGameNotFound))
        } else {
            cBack(Response.Success(game))
        }
    }

    override fun getNextTeamTurn(cBack: (Response<String>) -> Unit) {
        val game = gameDataSource.getGame()
        when {
            game == null -> cBack(Response.Error(ErrorType.ErrorGameNotFound))
            game.teamsPositions.isEmpty() -> cBack(Response.Error(ErrorType.ErrorTeamNotFound))
            else -> {
                val nextTeamTurn = game.teamsPositions.keys.elementAt(
                    (game.turnNumber % game.teamsPositions.size)
                )
                cBack(Response.Success(nextTeamTurn))
            }
        }
    }

    override fun getNextTeammateTurn(teamId: String, cBack: (Response<String>) -> Unit) {
        val team = gameDataSource.getTeams()[teamId]
        when {
            team == null -> cBack(Response.Error(ErrorType.ErrorTeamNotFound))
            team.teammatesNames.isEmpty() -> cBack(Response.Error(ErrorType.ErrorNoTeammatesFound))
            else -> {
                val teammatePosition = team.nextTeammateTurn % team.teammatesNames.size
                cBack(Response.Success(team.teammatesNames[teammatePosition]))
            }
        }
    }
}
