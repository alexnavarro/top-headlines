package com.alexandrenavarro.topheadlines.data.di

import com.alexandrenavarro.topheadlines.data.remote.NewsRemoteDataSource
import com.alexandrenavarro.topheadlines.data.remote.NewsRemoteDataSourceImpl
import com.alexandrenavarro.topheadlines.data.repository.NewsRepositoryImpl
import com.alexandrenavarro.topheadlines.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    @Binds
    @Singleton
    abstract fun bindsNewsRemoteDataSource(impl: NewsRemoteDataSourceImpl): NewsRemoteDataSource
}
