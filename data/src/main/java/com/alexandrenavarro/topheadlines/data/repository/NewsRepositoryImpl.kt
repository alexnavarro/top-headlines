package com.alexandrenavarro.topheadlines.data.repository

import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.core.result.map
import com.alexandrenavarro.topheadlines.data.mapper.toDomain
import com.alexandrenavarro.topheadlines.data.remote.NewsRemoteDataSource
import com.alexandrenavarro.topheadlines.domain.model.Article
import com.alexandrenavarro.topheadlines.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
) : NewsRepository {

    override suspend fun getTopHeadlines(): AppResult<List<Article>> =
        remoteDataSource.getTopHeadlines().map { it.toDomain() }
}
