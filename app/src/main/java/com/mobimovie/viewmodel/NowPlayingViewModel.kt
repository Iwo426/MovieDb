package com.mobimovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobimovie.repository.NowPlayingRepository
import com.mobimovie.response.NowPlayingResponse
import com.mobimovie.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    nowPlayingRepository: NowPlayingRepository
) : ViewModel() {

    private val _data = MutableLiveData<DataState<NowPlayingResponse>>()
    val data: LiveData<DataState<NowPlayingResponse>> = _data

    init {
        viewModelScope.launch {
            nowPlayingRepository.getNowPlaying()
                .onEach { dataState ->
                    _data.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }
}