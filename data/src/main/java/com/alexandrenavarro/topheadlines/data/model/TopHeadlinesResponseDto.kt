package com.alexandrenavarro.topheadlines.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TopHeadlinesResponseDto(
    val status: String?,
    val totalResults: Int?,
    val articles: List<ArticleDto>?,
)
