package com.mobimovie.service

import com.google.gson.JsonObject
import com.mobimovie.request.LoginRequest
import com.mobimovie.request.SessionRequest
import com.mobimovie.response.*
import retrofit2.http.*

interface MobiMovieApi {

    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("api_key") apiKey: String?): NowPlayingResponse

    @GET("authentication/token/new")
    suspend fun getToken(@Query("api_key") apiKey: String?): RequestTokenResponse

    @POST("authentication/token/validate_with_login")
    suspend fun logIn(
        @Query("api_key") apiKey: String?,
        @Body request: LoginRequest
    ): RequestTokenResponse

    @POST("authentication/session/new")
    suspend fun createSessionId(
        @Query("api_key") apiKey: String?,
        @Body request: SessionRequest
    ): CreateSessionIdResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("api_key") apiKey: String?): UpComingResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") id: Int?,
        @Query("api_key") apiKey: String?
    ): MovieDetailResponse

    @GET("account")
    suspend fun getAccountDetail(
        @Query("api_key") apiKey: String?,
        @Query("session_id") id: String?
    ): AccountDetailResponse

}