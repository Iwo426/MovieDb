package com.mobimovie.repository

import com.mobimovie.response.NowPlayingResponse
import com.mobimovie.response.UpComingResponse
import com.mobimovie.service.MobiMovieApi
import com.mobimovie.utils.DataState
import com.mobimovie.utils.MobiMovieConstants.API_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FavoriteListRepository @Inject constructor(
    private val api: MobiMovieApi,
) {
    suspend fun getFavList(
        id: Int,
        page: Int,
        sessionId: String
    ) : NowPlayingResponse{
            return  api.getFavoritelist(id, API_KEY, sessionId, page)
    }

}