package com.alexandrenavarro.topheadlines.data.repository

import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.core.result.map
import com.alexandrenavarro.topheadlines.data.mapper.toDomain
import com.alexandrenavarro.topheadlines.data.remote.NewsRemoteDataSource
import com.alexandrenavarro.topheadlines.domain.model.Article
import com.alexandrenavarro.topheadlines.domain.repository.NewsRepository
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource,
) : NewsRepository {

    private val articlesCache = ConcurrentHashMap<String, Article>()

    override suspend fun getTopHeadlines(): AppResult<List<Article>> =
        remoteDataSource.getTopHeadlines().map { it.toDomain() }
            .also { result ->
                if (result is AppResult.Success) {
                    result.data.forEach { article ->
                        if (article.id.isNotBlank()) {
                            articlesCache[article.id] = article
                        }
                    }
                }
            }

    override fun getArticleById(id: String): Article? = articlesCache[id]
}
