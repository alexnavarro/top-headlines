package com.alexandrenavarro.topheadlines.data.mapper

import com.alexandrenavarro.topheadlines.data.model.ArticleDto
import com.alexandrenavarro.topheadlines.domain.model.Article

fun ArticleDto.toDomain(): Article = Article(
    title = title.orEmpty(),
    description = description.orEmpty(),
    url = url.orEmpty(),
    imageUrl = urlToImage,
    publishedAt = publishedAt.orEmpty(),
    author = author,
    sourceName = source?.name.orEmpty(),
)

fun List<ArticleDto>.toDomain(): List<Article> = map { it.toDomain() }
