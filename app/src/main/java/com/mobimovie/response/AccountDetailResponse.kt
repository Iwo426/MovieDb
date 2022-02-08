package com.mobimovie.response

import com.mobimovie.model.Avatar

data class AccountDetailResponse(
    val avatar: Avatar,
    val id: Int,
    val include_adult: Boolean,
    val iso_3166_1: String,
    val iso_639_1: String,
    val name: String,
    val username: String
)