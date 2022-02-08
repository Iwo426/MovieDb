package com.mobimovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.mobimovie.repository.AddToFavoriteRepository
import com.mobimovie.repository.LoginRepository
import com.mobimovie.request.AddToFavoriteRequest
import com.mobimovie.request.LoginRequest
import com.mobimovie.response.CommonResponse
import com.mobimovie.response.RequestTokenResponse
import com.mobimovie.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFavoriteViewModel @Inject constructor(
    private val addToFavoriteRepository: AddToFavoriteRepository
) : ViewModel() {

    private val _data = MutableLiveData<DataState<CommonResponse>>()
    val data: LiveData<DataState<CommonResponse>> = _data

    fun addToFavorite(accountId : Int,sessionId:String,request: AddToFavoriteRequest) {
        viewModelScope.launch {
            addToFavoriteRepository.addToFavorite(accountId,sessionId,request)
                .onEach { dataState ->
                    _data.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }
}