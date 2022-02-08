package com.mobimovie.di

import com.mobimovie.repository.*
import com.mobimovie.service.MobiMovieApi
import com.mobimovie.viewmodel.GetFavoriteListViewModel
import com.mobimovie.viewmodel.GetWatchListViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideUpComingRepository(
        mobiMovieApi: MobiMovieApi,
    ): UpComingRepository {
        return UpComingRepository(mobiMovieApi)
    }

    @Singleton
    @Provides
    fun provideNowPlayingRepository(
        mobiMovieApi: MobiMovieApi,
    ): NowPlayingRepository {
        return NowPlayingRepository(mobiMovieApi)
    }

    @Singleton
    @Provides
    fun provideMovieDetailRepository(
        mobiMovieApi: MobiMovieApi,
    ): MovieDetailRepository {
        return MovieDetailRepository(mobiMovieApi)
    }

    @Singleton
    @Provides
    fun provideRequestTokenRepository(
        mobiMovieApi: MobiMovieApi,
    ): RequestTokenRepository {
        return RequestTokenRepository(mobiMovieApi)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(
        mobiMovieApi: MobiMovieApi,
    ): LoginRepository {
        return LoginRepository(mobiMovieApi)
    }

    @Singleton
    @Provides
    fun provideAccountDetailRepository(
        mobiMovieApi: MobiMovieApi,
    ): AccountDetailRepository {
        return AccountDetailRepository(mobiMovieApi)
    }

    @Singleton
    @Provides
    fun provideCreateSessionIdRepository(
        mobiMovieApi: MobiMovieApi,
    ): CreateSessionIdRepository {
        return CreateSessionIdRepository(mobiMovieApi)
    }

    @Singleton
    @Provides
    fun provideAddToFavoriteRepository(
        mobiMovieApi: MobiMovieApi,
    ): AddToFavoriteRepository {
        return AddToFavoriteRepository(mobiMovieApi)
    }

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideGetWatchListViewModel(
        mobiMovieApi: MobiMovieApi,
    ): GetWatchListViewModel {
        return GetWatchListViewModel(mobiMovieApi)
    }

    @ExperimentalCoroutinesApi
    @Singleton
    @Provides
    fun provideGetFavoriteListViewModel(
        mobiMovieApi: MobiMovieApi,
    ): GetFavoriteListViewModel {
        return GetFavoriteListViewModel(mobiMovieApi)
    }
}














