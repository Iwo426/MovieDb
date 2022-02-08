package com.mobimovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.mobimovie.repository.LoginRepository
import com.mobimovie.request.LoginRequest
import com.mobimovie.response.RequestTokenResponse
import com.mobimovie.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _data = MutableLiveData<DataState<RequestTokenResponse>>()
    val data: LiveData<DataState<RequestTokenResponse>> = _data

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            loginRepository.logIn(request)
                .onEach { dataState ->
                    _data.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }
}