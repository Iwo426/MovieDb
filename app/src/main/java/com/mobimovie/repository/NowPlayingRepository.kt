package com.mobimovie.repository

import com.mobimovie.response.NowPlayingResponse
import com.mobimovie.response.UpComingResponse
import com.mobimovie.service.MobiMovieApi
import com.mobimovie.utils.DataState
import com.mobimovie.utils.MobiMovieConstants.API_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NowPlayingRepository @Inject constructor(
    private val api: MobiMovieApi,
) {
    suspend fun getNowPlaying(): Flow<DataState<NowPlayingResponse>> = flow {
        emit(DataState.Loading)
        try {
            val response = api.getNowPlaying(API_KEY)
            emit(DataState.Success(response))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }
}