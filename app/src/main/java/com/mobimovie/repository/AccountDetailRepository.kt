package com.mobimovie.repository

import com.mobimovie.response.AccountDetailResponse
import com.mobimovie.service.MobiMovieApi
import com.mobimovie.utils.DataState
import com.mobimovie.utils.MobiMovieConstants.API_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AccountDetailRepository @Inject constructor(
    private val api: MobiMovieApi,
) {
    suspend fun getAccountDetail(
        key: String,
        sessionId: String
    ): Flow<DataState<AccountDetailResponse>> = flow {
        emit(DataState.Loading)
        try {
            val response = api.getAccountDetail(API_KEY, sessionId)
            emit(DataState.Success(response))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}