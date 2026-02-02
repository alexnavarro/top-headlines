package com.alexandrenavarro.topheadlines.domain.usecase

import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.core.result.map
import com.alexandrenavarro.topheadlines.domain.model.Article
import com.alexandrenavarro.topheadlines.domain.repository.NewsRepository
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: NewsRepository,
) {

    suspend operator fun invoke(): AppResult<List<Article>> =
        repository.getTopHeadlines().map { articles ->
            articles.sortedByDescending { article ->
                article.publishedAt.toSortableDate()
            }
        }

    private fun String.toSortableDate(): String =
        takeIf { it.isNotBlank() && it.isValidIsoDate() } ?: ""

    private fun String.isValidIsoDate(): Boolean =
        matches(ISO_DATE_REGEX)

    private companion object {
        val ISO_DATE_REGEX = Regex("""^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.*""")
    }
}
