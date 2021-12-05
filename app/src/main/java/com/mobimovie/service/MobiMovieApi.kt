package com.mobimovie.service

import com.mobimovie.response.MovieDetailResponse
import com.mobimovie.response.NowPlayingResponse
import com.mobimovie.response.UpComingResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MobiMovieApi {

    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("api_key") apiKey: String?): NowPlayingResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("api_key") apiKey: String?): UpComingResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") id: Int?,
        @Query("api_key") apiKey: String?
    ): MovieDetailResponse

}