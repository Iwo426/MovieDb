package com.mobimovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobimovie.repository.AccountDetailRepository
import com.mobimovie.repository.CreateSessionIdRepository
import com.mobimovie.request.SessionRequest
import com.mobimovie.response.AccountDetailResponse
import com.mobimovie.response.CreateSessionIdResponse
import com.mobimovie.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val createSessionIdRepository: CreateSessionIdRepository
) : ViewModel() {

    private val _data = MutableLiveData<DataState<CreateSessionIdResponse>>()
    val data: LiveData<DataState<CreateSessionIdResponse>> = _data

    fun getSessionId(key:String,request :SessionRequest) {
        viewModelScope.launch {
            createSessionIdRepository.getSessionId(key,request)
                .onEach { dataState ->
                    _data.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }
}