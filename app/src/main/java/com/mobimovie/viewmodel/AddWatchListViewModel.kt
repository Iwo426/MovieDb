package com.mobimovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobimovie.repository.AddToWatchListRepository
import com.mobimovie.request.AddToWatchListRequest
import com.mobimovie.response.CommonResponse
import com.mobimovie.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWatchListViewModel @Inject constructor(
    private val addToWatchListRepository: AddToWatchListRepository
) : ViewModel() {

    private val _data = MutableLiveData<DataState<CommonResponse>>()
    val data: LiveData<DataState<CommonResponse>> = _data

    fun addToWatchlist(accountId : Int,sessionId:String,request: AddToWatchListRequest) {
        viewModelScope.launch {
            addToWatchListRepository.addToWatchList(accountId,sessionId,request)
                .onEach { dataState ->
                    _data.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }
}