package com.alexandrenavarro.topheadlines.domain.repository

import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.domain.model.Article

interface NewsRepository {
    suspend fun getTopHeadlines(): AppResult<List<Article>>
    fun getArticleById(id: String): Article?
}
