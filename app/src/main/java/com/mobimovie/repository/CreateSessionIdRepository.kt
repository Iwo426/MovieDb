package com.mobimovie.repository

import com.mobimovie.request.SessionRequest
import com.mobimovie.response.AccountDetailResponse
import com.mobimovie.response.CreateSessionIdResponse
import com.mobimovie.service.MobiMovieApi
import com.mobimovie.utils.DataState
import com.mobimovie.utils.MobiMovieConstants.API_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateSessionIdRepository @Inject constructor(
    private val api: MobiMovieApi,
) {
    suspend fun getSessionId(
        key: String,
        request :SessionRequest
    ): Flow<DataState<CreateSessionIdResponse>> = flow {
        emit(DataState.Loading)
        try {
            val response = api.createSessionId(API_KEY, request)
            emit(DataState.Success(response))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}