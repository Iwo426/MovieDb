package com.mobimovie.request

import org.json.JSONObject

data class LoginRequest(
    val username: String,
    val password: String,
    val request_token: String
): JSONObject()