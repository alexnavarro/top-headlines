package com.alexandrenavarro.topheadlines.data.mapper

import com.alexandrenavarro.topheadlines.data.model.ArticleDto
import com.alexandrenavarro.topheadlines.domain.model.Article

fun ArticleDto.toDomain(): Article {
    val sourceId = source?.id.orEmpty()
    val articleUrl = url.orEmpty()
    return Article(
        id = listOf(sourceId, articleUrl).filter { it.isNotEmpty() }.joinToString("_"),
        sourceId = sourceId,
        title = title.orEmpty(),
        description = description.orEmpty(),
        url = articleUrl,
        imageUrl = urlToImage,
        publishedAt = publishedAt.orEmpty(),
        author = author,
        sourceName = source?.name.orEmpty(),
        content = content.orEmpty().removeTrailingCharCount(),
    )
}

fun List<ArticleDto>.toDomain(): List<Article> = map { it.toDomain() }

private val TRAILING_CHAR_COUNT_REGEX = Regex("""\s*\[\+\d+ chars]$""")

private fun String.removeTrailingCharCount(): String =
    replace(TRAILING_CHAR_COUNT_REGEX, "")
