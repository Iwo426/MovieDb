package com.mobimovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobimovie.repository.MovieDetailRepository
import com.mobimovie.repository.NowPlayingRepository
import com.mobimovie.response.MovieDetailResponse
import com.mobimovie.response.NowPlayingResponse
import com.mobimovie.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieDetailRepository: MovieDetailRepository
) : ViewModel() {

    private val _data = MutableLiveData<DataState<MovieDetailResponse>>()
    val data: LiveData<DataState<MovieDetailResponse>> = _data

    fun fetchMovieDetails(id: Int) {
        viewModelScope.launch {
            movieDetailRepository.getMovieDetail(id)
                .onEach { dataState ->
                    _data.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }
}