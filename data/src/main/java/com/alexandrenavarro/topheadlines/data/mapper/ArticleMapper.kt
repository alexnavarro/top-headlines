package com.alexandrenavarro.topheadlines.data.mapper

import com.alexandrenavarro.topheadlines.data.model.ArticleDto
import com.alexandrenavarro.topheadlines.domain.model.Article

fun ArticleDto.toDomain(): Article = Article(
    id = url.orEmpty(),
    title = title.orEmpty(),
    description = description.orEmpty(),
    url = url.orEmpty(),
    imageUrl = urlToImage,
    publishedAt = publishedAt.orEmpty(),
    author = author,
    sourceName = source?.name.orEmpty(),
    content = content.orEmpty().removeTrailingCharCount(),
)

fun List<ArticleDto>.toDomain(): List<Article> = map { it.toDomain() }

private val TRAILING_CHAR_COUNT_REGEX = Regex("""\s*\[\+\d+ chars]$""")

private fun String.removeTrailingCharCount(): String =
    replace(TRAILING_CHAR_COUNT_REGEX, "")
