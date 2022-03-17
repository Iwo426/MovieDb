package com.mobimovie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobimovie.repository.FavoriteListRepository
import com.mobimovie.repository.NowPlayingRepository
import com.mobimovie.response.NowPlayingResponse
import com.mobimovie.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val favoriteListRepository: FavoriteListRepository
) : ViewModel() {
    suspend fun getFavList(
        id: Int,
        page: Int,
        sessionId: String,
    ) :NowPlayingResponse {
        return favoriteListRepository.getFavList(id, page, sessionId)
    }
}