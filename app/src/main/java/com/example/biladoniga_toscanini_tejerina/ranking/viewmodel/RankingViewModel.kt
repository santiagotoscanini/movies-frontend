package com.example.biladoniga_toscanini_tejerina.ranking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.ErrorType
import com.data.Response
import com.data.models.Movie
import com.data.repositories.movie.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RankingViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _showLoader = MutableLiveData<Boolean>()
    private val _showError = MutableLiveData<ErrorType>()
    private val _rankingByGuessed = MutableLiveData<List<Movie>>()

    val rankingByGuessed: LiveData<List<Movie>>
        get() = _rankingByGuessed
    val showLoader: LiveData<Boolean>
        get() = _showLoader
    val showError: LiveData<ErrorType>
        get() = _showError

    fun getRankingByGuessed() {
        _showLoader.postValue(true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movieRepository.getMoviesByGuessed { response ->
                    if (response is Response.Success) {
                        _rankingByGuessed.postValue(response.data)
                    } else {
                        _showError.postValue((response as Response.Error).error)
                    }
                    _showLoader.postValue(false)
                }
            }
        }
    }
}