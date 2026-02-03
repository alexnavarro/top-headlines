package com.alexandrenavarro.topheadlines.ui.headlines

import com.alexandrenavarro.topheadlines.domain.model.Article

sealed interface HeadlinesUiState {
    data object Loading : HeadlinesUiState
    data class Success(val articles: List<Article>) : HeadlinesUiState
    data class Error(val message: String) : HeadlinesUiState
}
