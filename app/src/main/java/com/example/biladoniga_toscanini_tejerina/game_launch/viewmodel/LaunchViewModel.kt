package com.example.biladoniga_toscanini_tejerina.game_launch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.Response
import com.data.models.Team
import com.data.repositories.TeamRepository
import com.example.biladoniga_toscanini_tejerina.commons.TeamStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LaunchViewModel(private val teamRepository: TeamRepository) : ViewModel() {

    private val _firstTeam = MutableLiveData<Response<Team>>()
    private val _secondTeam = MutableLiveData<Response<Team>>()
    private val _thirdTeam = MutableLiveData<Response<Team>>()

    private val _showLoader = MutableLiveData<Boolean>()

    private val _firstTeamStatus = MutableLiveData<TeamStatus>()
    private val _secondTeamStatus = MutableLiveData<TeamStatus>()
    private val _thirdTeamStatus = MutableLiveData<TeamStatus>()

    val firstTeam: LiveData<Response<Team>>
        get() = _firstTeam

    val secondTeam: LiveData<Response<Team>>
        get() = _secondTeam

    val thirdTeam: LiveData<Response<Team>>
        get() = _thirdTeam

    val showLoader: LiveData<Boolean>
        get() = _showLoader

    val firstTeamStatus: LiveData<TeamStatus>
        get() = _firstTeamStatus

    val secondTeamStatus: LiveData<TeamStatus>
        get() = _secondTeamStatus

    val thirdTeamStatus: LiveData<TeamStatus>
        get() = _thirdTeamStatus

    fun getTeamsStatus() {
        _showLoader.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getFirstTeamStatus()
                getSecondTeamStatus()
                getThirdTeamStatus()
                _showLoader.postValue(false)
            }
        }
    }

    private fun getFirstTeamStatus() {
        teamRepository.getTeam(Team.firstTeam) { response ->
            val team = (response as Response.Success<Team>).data
            _firstTeamStatus.postValue(getStatusForTeam(team))
        }
    }

    private fun getSecondTeamStatus() {
        teamRepository.getTeam(Team.secondTeam) { response ->
            val team = (response as Response.Success<Team>).data
            _secondTeamStatus.postValue(getStatusForTeam(team))
        }
    }

    private fun getThirdTeamStatus() {
        teamRepository.getTeam(Team.secondTeam) { response ->
            val team = (response as Response.Success<Team>).data
            _thirdTeamStatus.postValue(getStatusForTeam(team))
        }
    }

    private fun getStatusForTeam(team: Team): TeamStatus {
        return when (team.teammatesNames.size) {
            0 -> TeamStatus.NO_READY
            1 -> TeamStatus.ALMOST_READY
            2 -> TeamStatus.READY
            else -> TeamStatus.NO_READY
        }
    }

}
