package com.mobimovie.response

import com.mobimovie.model.UpcomingModel

data class UpComingResponse(
    val page: Int,
    val results: List<UpcomingModel>
)