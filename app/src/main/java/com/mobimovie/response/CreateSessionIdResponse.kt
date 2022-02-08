package com.mobimovie.response

data class CreateSessionIdResponse(
    val session_id: String,
    val success: Boolean
)