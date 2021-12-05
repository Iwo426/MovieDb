package com.mobimovie.di

import com.mobimovie.repository.MovieDetailRepository
import com.mobimovie.repository.NowPlayingRepository
import com.mobimovie.repository.UpComingRepository
import com.mobimovie.service.MobiMovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideUpComingRepository(
        mobiMovieApi: MobiMovieApi,
    ): UpComingRepository {
        return UpComingRepository( mobiMovieApi)
    }

    @Singleton
    @Provides
    fun provideNowPlayingRepository(
        mobiMovieApi: MobiMovieApi,
    ): NowPlayingRepository {
        return NowPlayingRepository( mobiMovieApi)
    }

    @Singleton
    @Provides
    fun provideMovieDetailRepository(
        mobiMovieApi: MobiMovieApi,
    ): MovieDetailRepository {
        return MovieDetailRepository( mobiMovieApi)
    }
}














