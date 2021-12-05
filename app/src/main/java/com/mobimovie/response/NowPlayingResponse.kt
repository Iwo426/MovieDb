package com.mobimovie.response

import com.mobimovie.model.NowPlayingModel

data class NowPlayingResponse(
    val page: Int,
    val results: List<NowPlayingModel>
)