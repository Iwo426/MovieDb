package com.mobimovie.viewmodel

import androidx.lifecycle.*
import com.mobimovie.utils.DataState
import com.mobimovie.repository.UpComingRepository
import com.mobimovie.response.UpComingResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpComingViewModel @Inject constructor(
    upComingRepository: UpComingRepository
) : ViewModel() {

    private val _data = MutableLiveData<DataState<UpComingResponse>>()
    val data: LiveData<DataState<UpComingResponse>> = _data

    init {
        viewModelScope.launch {
            upComingRepository.getUpcomingMovies()
                .onEach { dataState ->
                    _data.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }
}