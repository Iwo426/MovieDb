package com.mobimovie.repository

import com.mobimovie.response.MovieDetailResponse
import com.mobimovie.response.RequestTokenResponse
import com.mobimovie.service.MobiMovieApi
import com.mobimovie.utils.DataState
import com.mobimovie.utils.MobiMovieConstants.API_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RequestTokenRepository @Inject constructor(
    private val api: MobiMovieApi,
) {

    suspend fun getToken(key: String): Flow<DataState<RequestTokenResponse>> = flow {
        emit(DataState.Loading)
        try {
            val response = api.getToken(API_KEY)
            emit(DataState.Success(response))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}