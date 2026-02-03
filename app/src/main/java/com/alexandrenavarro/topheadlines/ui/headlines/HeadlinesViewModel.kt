package com.alexandrenavarro.topheadlines.ui.headlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.data.di.NewsProviderTitle
import com.alexandrenavarro.topheadlines.domain.usecase.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeadlinesViewModel @Inject constructor(
    private val getTopHeadlines: GetTopHeadlinesUseCase,
    @NewsProviderTitle val providerTitle: String,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HeadlinesUiState>(HeadlinesUiState.Loading)
    val uiState: StateFlow<HeadlinesUiState> = _uiState.asStateFlow()

    init {
        loadHeadlines()
    }

    fun loadHeadlines() {
        viewModelScope.launch {
            _uiState.value = HeadlinesUiState.Loading
            _uiState.value = when (val result = getTopHeadlines()) {
                is AppResult.Success -> HeadlinesUiState.Success(result.data)
                is AppResult.Error -> HeadlinesUiState.Error(
                    result.exception.message ?: "Unknown error"
                )
            }
        }
    }
}
