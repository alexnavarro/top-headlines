package com.alexandrenavarro.topheadlines.ui.auth

sealed interface AuthUiState {
    data object Required : AuthUiState
    data object Authenticated : AuthUiState
}
