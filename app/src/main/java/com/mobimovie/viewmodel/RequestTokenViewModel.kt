package com.mobimovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobimovie.repository.RequestTokenRepository
import com.mobimovie.response.RequestTokenResponse
import com.mobimovie.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestTokenViewModel @Inject constructor(
    private val requestTokenRepository: RequestTokenRepository
) : ViewModel() {

    private val _data = MutableLiveData<DataState<RequestTokenResponse>>()
    val data: LiveData<DataState<RequestTokenResponse>> = _data

    fun getToken(key: String) {
        viewModelScope.launch {
            requestTokenRepository.getToken(key)
                .onEach { dataState ->
                    _data.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }
}