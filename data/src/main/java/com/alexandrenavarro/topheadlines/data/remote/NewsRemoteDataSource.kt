package com.alexandrenavarro.topheadlines.data.remote

import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.data.model.ArticleDto

interface NewsRemoteDataSource {
    suspend fun getTopHeadlines(): AppResult<List<ArticleDto>>
}
