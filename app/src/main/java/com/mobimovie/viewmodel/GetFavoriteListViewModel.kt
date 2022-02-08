package com.mobimovie.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mobimovie.model.NowPlayingModel
import com.mobimovie.repository.GetFavoritelistRepository
import com.mobimovie.repository.GetWatchlistRepository
import com.mobimovie.service.MobiMovieApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class GetFavoriteListViewModel
@Inject
constructor(
    private val api: MobiMovieApi
) : ViewModel() {

    fun getFavoriteList(
        id: Int,
        page: Int,
        sessionId: String,
    ): Flow<PagingData<NowPlayingModel>> {

        return Pager(
            config = PagingConfig(pageSize = 5, prefetchDistance = 2),
            pagingSourceFactory = {
                GetFavoritelistRepository(api, id, page, sessionId)
            }).flow
    }
}



















