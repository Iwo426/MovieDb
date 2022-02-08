package com.mobimovie.repository

import com.google.gson.JsonObject
import com.mobimovie.request.LoginRequest
import com.mobimovie.response.RequestTokenResponse
import com.mobimovie.service.MobiMovieApi
import com.mobimovie.utils.DataState
import com.mobimovie.utils.MobiMovieConstants.API_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val api: MobiMovieApi,
) {

    suspend fun logIn(request: LoginRequest): Flow<DataState<RequestTokenResponse>> = flow {
        emit(DataState.Loading)
        try {
            val response = api.logIn(API_KEY, request)
            emit(DataState.Success(response))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}