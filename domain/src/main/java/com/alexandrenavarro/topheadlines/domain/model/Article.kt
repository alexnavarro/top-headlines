package com.alexandrenavarro.topheadlines.domain.model

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String?,
    val publishedAt: String,
    val author: String?,
    val sourceName: String,
    val content: String,
)
