package com.mobimovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobimovie.repository.AccountDetailRepository
import com.mobimovie.response.AccountDetailResponse
import com.mobimovie.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountDetailViewModel @Inject constructor(
    private val accountDetailRepository: AccountDetailRepository
) : ViewModel() {

    private val _data = MutableLiveData<DataState<AccountDetailResponse>>()
    val data: LiveData<DataState<AccountDetailResponse>> = _data

    fun getAccountDetail(key:String,sessionId :String) {
        viewModelScope.launch {
            accountDetailRepository.getAccountDetail(key,sessionId)
                .onEach { dataState ->
                    _data.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }
}