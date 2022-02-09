package com.mobimovie.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mobimovie.model.NowPlayingModel
import com.mobimovie.repository.GetWatchlistRepository
import com.mobimovie.repository.SearchMovieRepository
import com.mobimovie.service.MobiMovieApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class SearchListViewModel
@Inject
constructor(
    private val api: MobiMovieApi
) : ViewModel() {

    fun searchMovie(
        page: Int,
        query: String,
    ): Flow<PagingData<NowPlayingModel>> {
        return Pager(
            config = PagingConfig(pageSize = 15, prefetchDistance = 2),
            pagingSourceFactory = {
                SearchMovieRepository(api, page, query)
            }).flow
    }
}



















