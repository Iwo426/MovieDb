package com.mobimovie.repository

import com.mobimovie.request.AddToFavoriteRequest
import com.mobimovie.request.AddToWatchListRequest
import com.mobimovie.response.CommonResponse
import com.mobimovie.service.MobiMovieApi
import com.mobimovie.utils.DataState
import com.mobimovie.utils.MobiMovieConstants.API_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddToWatchListRepository @Inject constructor(
    private val api: MobiMovieApi,
) {
    suspend fun addToWatchList(
        accountId : Int,
        sessionId: String,
        request : AddToWatchListRequest
    ): Flow<DataState<CommonResponse>> = flow {
        emit(DataState.Loading)
        try {
            val response = api.addToWatchlist(accountId,API_KEY, sessionId,request)
            emit(DataState.Success(response))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}