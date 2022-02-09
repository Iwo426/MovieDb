package com.mobimovie.repository

import androidx.paging.PagingSource
import com.mobimovie.model.NowPlayingModel
import com.mobimovie.response.NowPlayingResponse
import com.mobimovie.service.MobiMovieApi
import com.mobimovie.utils.MobiMovieConstants.API_KEY

class SearchMovieRepository(
    private val api: MobiMovieApi,
    var page: Int,
    private val query: String
) : PagingSource<Int, NowPlayingModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NowPlayingModel> {
        return try {
            val nextPageNumber = params.key ?: 0
            val response: NowPlayingResponse = api.searchMovie(API_KEY, query, page)
            page += 1
            LoadResult.Page(
                data = response.results,
                prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                nextKey = if (nextPageNumber < response.results.size) nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}