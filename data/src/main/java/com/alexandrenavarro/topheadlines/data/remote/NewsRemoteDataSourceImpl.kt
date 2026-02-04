package com.alexandrenavarro.topheadlines.data.remote

import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.data.di.NewsApiKey
import com.alexandrenavarro.topheadlines.data.di.NewsSource
import com.alexandrenavarro.topheadlines.data.model.ArticleDto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRemoteDataSourceImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    @param:NewsSource private val source: String,
    @param:NewsApiKey private val apiKey: String,
) : NewsRemoteDataSource {

    override suspend fun getTopHeadlines(): AppResult<List<ArticleDto>> =
        try {
            val response = newsApiService.getTopHeadlines(
                sources = source,
                apiKey = apiKey,
            )
            AppResult.Success(response.articles.orEmpty())
        } catch (e: IOException) {
            AppResult.Error(e)
        } catch (e: HttpException) {
            AppResult.Error(e)
        }
}
