package com.alexandrenavarro.topheadlines.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alexandrenavarro.topheadlines.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: NewsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(resolveState(savedStateHandle, repository))
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private fun resolveState(
        savedStateHandle: SavedStateHandle,
        repository: NewsRepository,
    ): DetailUiState {
        val articleId: String = checkNotNull(savedStateHandle[ARTICLE_ID_KEY])
        val article = repository.getArticleById(articleId)
        return if (article != null) {
            DetailUiState.Success(article)
        } else {
            DetailUiState.Error("Article not found")
        }
    }

    private companion object {
        const val ARTICLE_ID_KEY = "articleId"
    }
}
