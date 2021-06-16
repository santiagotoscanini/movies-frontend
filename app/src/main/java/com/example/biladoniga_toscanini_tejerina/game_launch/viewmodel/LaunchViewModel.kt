package com.example.biladoniga_toscanini_tejerina.game_launch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.Response
import com.data.models.Game
import com.data.models.Team
import com.data.repositories.team.TeamRepository
import com.example.biladoniga_toscanini_tejerina.commons.TeamStatus
import kotlinx.coroutines.*

class LaunchViewModel(private val teamRepository: TeamRepository) : ViewModel() {

    private var countOfReady = 0

    private val _team = MutableLiveData<Response<Team>>()

    private val _showLoader = MutableLiveData<Boolean>()

    private val _firstTeamStatus = MutableLiveData<TeamStatus>()
    private val _secondTeamStatus = MutableLiveData<TeamStatus>()
    private val _thirdTeamStatus = MutableLiveData<TeamStatus>()
    private val _readyToStartGame = MutableLiveData<Boolean>()

    val team: LiveData<Response<Team>>
        get() = _team

    val showLoader: LiveData<Boolean>
        get() = _showLoader

    val firstTeamStatus: LiveData<TeamStatus>
        get() = _firstTeamStatus

    val secondTeamStatus: LiveData<TeamStatus>
        get() = _secondTeamStatus

    val thirdTeamStatus: LiveData<TeamStatus>
        get() = _thirdTeamStatus

    val readyToStartGame: LiveData<Boolean>
        get() = _readyToStartGame

    fun getTeamsStatus() {
        _showLoader.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                countOfReady = 0
                getFirstTeamStatus()
                getSecondTeamStatus()
                getThirdTeamStatus()
                validateStates()
                _showLoader.postValue(false)
            }
        }
    }

    private fun getFirstTeamStatus() {
        teamRepository.getTeam(Team.firstTeam) { response ->
            val team = (response as Response.Success<Team>).data
            val teamStatus = getStatusForTeam(team)
            if(teamStatus == TeamStatus.READY) countOfReady++
            _firstTeamStatus.postValue(teamStatus)
        }
    }

    private fun getSecondTeamStatus() {
        teamRepository.getTeam(Team.secondTeam) { response ->
            val team = (response as Response.Success<Team>).data
            val teamStatus = getStatusForTeam(team)
            if(teamStatus == TeamStatus.READY) countOfReady++
            _secondTeamStatus.postValue(teamStatus)
        }
    }

    private fun getThirdTeamStatus() {
        teamRepository.getTeam(Team.thirdTeam) { response ->
            val team = (response as Response.Success<Team>).data
            val teamStatus = getStatusForTeam(team)
            if(teamStatus == TeamStatus.READY) countOfReady++
            _thirdTeamStatus.postValue(teamStatus)
        }
    }

    private fun getStatusForTeam(team: Team): TeamStatus {
        var countOfValidNames = 0
        for (teammate in team.teammatesNames) {
            if (teammate.isNotBlank()) {
                countOfValidNames++
            }
        }
        return when (countOfValidNames) {
            0 -> TeamStatus.NO_READY
            1 -> TeamStatus.LITLLE_READY
            2 -> if (team.image != null) TeamStatus.READY else TeamStatus.ALMOST_READY
            else -> TeamStatus.NO_READY
        }
    }

    fun saveTeam(team: Team) {
        _showLoader.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                teamRepository.saveTeam(team)
                _showLoader.postValue(false)
            }
        }
    }

    fun getTeam(teamId: String) {
        _showLoader.postValue(true)
        teamRepository.getTeam(teamId) { response ->
            _team.postValue(response)
            _showLoader.postValue(false)
        }
    }

    private fun validateStates() {
        if (countOfReady >= 2) {
            _readyToStartGame.postValue(true)
        } else {
            _readyToStartGame.postValue(false)
        }
    }

    fun createGame(){
        _showLoader.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                teamRepository.getTeams(){ response ->
                    val teams = (response as Response.Success<List<Team>>).data
                    createAndSaveGame(teams)
                    _showLoader.postValue(false)
                }
            }
        }
    }

    private fun createAndSaveGame(teams: List<Team>){
        val game = Game()
        for(team in teams){
            if(getStatusForTeam(team) == TeamStatus.READY){
                game.teamsPositions[team.id] = 0
            }
        }
        teamRepository.createOrUpdateGame(game)
    }
}
