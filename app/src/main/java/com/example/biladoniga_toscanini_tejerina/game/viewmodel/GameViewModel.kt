package com.example.biladoniga_toscanini_tejerina.game.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.Response
import com.data.models.Game
import com.data.models.Movie
import com.data.models.Team
import com.data.repositories.movie.MovieRepository
import com.data.repositories.team.TeamRepository
import com.example.biladoniga_toscanini_tejerina.commons.MAX_LEVELS
import com.example.biladoniga_toscanini_tejerina.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(
    private val teamRepository: TeamRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _showLoader = MutableLiveData<Boolean>()
    private val _showError = MutableLiveData<Response<Nothing>>()

    private val _firstTeamPosition = MutableLiveData<Int>()
    private val _secondTeamPosition = MutableLiveData<Int>()
    private val _thirdTeamPosition = MutableLiveData<Int>()
    private val _firstTeam = SingleLiveEvent<Team>()
    private val _secondTeam = SingleLiveEvent<Team>()
    private val _thirdTeam = SingleLiveEvent<Team>()
    private val _nextTeamTurn = SingleLiveEvent<String>()
    private val _nextTeammateTurn = MutableLiveData<String>()
    private val _movie = MutableLiveData<Movie>()
    private val _gameFinished = MutableLiveData<Boolean>()

    val firstTeam: LiveData<Team>
        get() = _firstTeam

    val secondTeam: LiveData<Team>
        get() = _secondTeam

    val thirdTeam: LiveData<Team>
        get() = _thirdTeam

    val showLoader: LiveData<Boolean>
        get() = _showLoader

    val showError: LiveData<Response<Nothing>>
        get() = _showError

    val firstTeamPosition: LiveData<Int>
        get() = _firstTeamPosition

    val secondTeamPosition: LiveData<Int>
        get() = _secondTeamPosition

    val thirdTeamPosition: LiveData<Int>
        get() = _thirdTeamPosition

    val nexTeamTurn: LiveData<String>
        get() = _nextTeamTurn

    val nextTeammateTurn: LiveData<String>
        get() = _nextTeammateTurn

    val movie: LiveData<Movie>
        get() = _movie

    val gameFinished: LiveData<Boolean>
        get() = _gameFinished

    fun getTeamsPositions() {
        _showLoader.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                teamRepository.getGame { response ->
                    if (response is Response.Success<Game>) {
                        setTeamsPositions(response)
                    } else {
                        _showError.postValue(response as Response.Error)
                    }
                    _showLoader.postValue(false)
                }
            }
        }
    }

    private fun setTeamsPositions(response: Response.Success<Game>) {
        for (teamPosition in response.data.teamsPositions) {
            teamPosition.value?.let { position ->
                setTeamPosition(teamPosition.key, position)
                checkIfGameEnded(position)
            }
        }
    }

    private fun setTeamPosition(teamId: String, position: Int) {
        when (teamId) {
            Team.firstTeam -> _firstTeamPosition.postValue(position)
            Team.secondTeam -> _secondTeamPosition.postValue(position)
            Team.thirdTeam -> _thirdTeamPosition.postValue(position)
        }
    }

    private fun checkIfGameEnded(position: Int) {
        if(position == MAX_LEVELS){
            cleanGameData()
        }else{
            _gameFinished.postValue(false);
        }
    }

    fun getTeams() {
        getFirstTeam()
        getSecondTeam()
        getThirdTeam()
    }

    private fun getFirstTeam() {
        _showLoader.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                teamRepository.getTeam(Team.firstTeam) { response ->
                    if (response is Response.Success<Team> && isTeamReady(response.data)) {
                        _firstTeam.postValue(response.data)
                    }
                    _showLoader.postValue(false)
                }
            }
        }
    }

    private fun getSecondTeam() {
        _showLoader.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                teamRepository.getTeam(Team.secondTeam) { response ->
                    if (response is Response.Success<Team> && isTeamReady(response.data)) {
                        _secondTeam.postValue(response.data)
                    }
                    _showLoader.postValue(false)
                }
            }
        }
    }

    private fun getThirdTeam() {
        _showLoader.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                teamRepository.getTeam(Team.thirdTeam) { response ->
                    if (response is Response.Success<Team> && isTeamReady(response.data)) {
                        _thirdTeam.postValue(response.data)
                    }
                    _showLoader.postValue(false)
                }
            }
        }
    }

    private fun isTeamReady(team: Team): Boolean {
        var countOfValidNames = 0
        for (teammate in team.teammatesNames) {
            if (teammate.isNotBlank()) {
                countOfValidNames++
            }
        }
        return countOfValidNames > 1 && team.image != null
    }

    fun getNextTeamTurn() {
        _showLoader.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                teamRepository.getNextTeamTurn { response ->
                    if (response is Response.Success<String>) {
                        _nextTeamTurn.postValue(response.data)
                    } else {
                        _showError.postValue(response as Response.Error)
                    }
                    _showLoader.postValue(false)
                }
            }
        }
    }

    fun getNextTeammateTurn(teamId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                teamRepository.getNextTeammateTurn(teamId) { response ->
                    if (response is Response.Success<String>) {
                        _nextTeammateTurn.postValue(response.data)
                    } else {
                        _showError.postValue(response as Response.Error)
                    }
                }
            }
        }
    }

    private fun loadMovie() {
        movieRepository.getMovie { response ->
            if (response is Response.Success<Movie>) {
                _movie.postValue(response.data)
            } else {
                _showError.postValue(response as Response.Error)
            }
            _showLoader.postValue(false)
        }
    }

    fun getMovie() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loadMovie()
            }
        }
    }

    fun increaseTurnTeamPosition() {
        updateTeamPositionAndTurn(_nextTeamTurn.value)
        updateTeammateTurn()
    }

    fun increaseTeamPosition(teamId: String?) {
        updateTeamPositionAndTurn(teamId)
        updateTeammateTurn()
    }

    private fun updateTeammateTurn() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _nextTeamTurn.value?.let {
                    teamRepository.getTeam(it) { team ->
                        if (team is Response.Success) {
                            ++team.data.nextTeammateTurn
                            teamRepository.saveTeam(team.data)
                        }
                    }
                }
            }
        }
    }

    private fun updateTeamPositionAndTurn(teamId: String?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                teamRepository.getGame { game ->
                    if (game is Response.Success) {
                        teamId?.let {
                            val teamPosition = (game.data.teamsPositions[teamId] ?: 0) + 1
                            game.data.teamsPositions.replace(teamId, teamPosition)
                            updateMovieGuessed()
                        }
                        game.data.turnNumber++
                        teamRepository.createOrUpdateGame(game.data)
                    }
                }
            }
        }
    }

    private fun cleanGameData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                teamRepository.cleanGame()
                teamRepository.cleanTeam()
                _gameFinished.postValue(true);
            }
        }
    }

    private fun updateMovieGuessed(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _movie.value?.let {
                    movieRepository.updateMovieGuessed(it)
                }
            }
        }
    }
}
