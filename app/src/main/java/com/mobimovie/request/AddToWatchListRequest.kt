package com.mobimovie.request

data class AddToWatchListRequest(
    val watchlist: Boolean,
    val media_id: Int,
    val media_type: String
)