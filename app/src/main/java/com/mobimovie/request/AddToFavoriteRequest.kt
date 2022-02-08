package com.mobimovie.request

data class AddToFavoriteRequest(
    val favorite: Boolean,
    val media_id: Int,
    val media_type: String
)