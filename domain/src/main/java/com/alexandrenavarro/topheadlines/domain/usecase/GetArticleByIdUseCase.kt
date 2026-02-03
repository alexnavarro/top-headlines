package com.alexandrenavarro.topheadlines.domain.usecase

import com.alexandrenavarro.topheadlines.domain.model.Article
import com.alexandrenavarro.topheadlines.domain.repository.NewsRepository
import javax.inject.Inject

class GetArticleByIdUseCase @Inject constructor(
    private val repository: NewsRepository,
) {

    operator fun invoke(id: String): Article? =
        repository.getArticleById(id)
}
