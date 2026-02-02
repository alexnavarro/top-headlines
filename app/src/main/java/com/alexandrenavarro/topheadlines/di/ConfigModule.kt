package com.alexandrenavarro.topheadlines.di

import com.alexandrenavarro.topheadlines.BuildConfig
import com.alexandrenavarro.topheadlines.data.di.NewsApiKey
import com.alexandrenavarro.topheadlines.data.di.NewsSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    @Provides
    @NewsSource
    fun providesNewsSource(): String = BuildConfig.NEWS_SOURCE

    @Provides
    @NewsApiKey
    fun providesNewsApiKey(): String = BuildConfig.NEWS_API_KEY
}
