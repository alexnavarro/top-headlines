package com.alexandrenavarro.topheadlines.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alexandrenavarro.topheadlines.domain.usecase.GetArticleByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getArticleById: GetArticleByIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        val articleId: String = checkNotNull(savedStateHandle[ARTICLE_ID_KEY])
        val article = getArticleById(articleId)
        _uiState.value = if (article != null) {
            DetailUiState.Success(article)
        } else {
            DetailUiState.Error("Article not found")
        }
    }

    private companion object {
        const val ARTICLE_ID_KEY = "articleId"
    }
}
