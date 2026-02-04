package com.alexandrenavarro.topheadlines.ui.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val biometricAuthenticator: BiometricAuthenticator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(resolveInitialState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onAuthenticationSucceeded() {
        _uiState.value = AuthUiState.Authenticated
    }

    fun isBiometricAvailable(): Boolean = biometricAuthenticator.canAuthenticate()

    private fun resolveInitialState(): AuthUiState =
        if (biometricAuthenticator.canAuthenticate()) {
            AuthUiState.Required
        } else {
            AuthUiState.Authenticated
        }
}
