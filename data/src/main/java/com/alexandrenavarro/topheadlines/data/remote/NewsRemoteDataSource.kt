package com.alexandrenavarro.topheadlines.data.remote

import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.data.di.NewsApiKey
import com.alexandrenavarro.topheadlines.data.di.NewsSource
import com.alexandrenavarro.topheadlines.data.model.ArticleDto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

open class NewsRemoteDataSource @Inject constructor(
    private val newsApiService: NewsApiService,
    @NewsSource private val source: String,
    @NewsApiKey private val apiKey: String,
) {

    open suspend fun getTopHeadlines(): AppResult<List<ArticleDto>> =
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
