package com.alexandrenavarro.topheadlines.ui.detail

import com.alexandrenavarro.topheadlines.domain.model.Article

sealed interface DetailUiState {
    data class Success(val article: Article) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
