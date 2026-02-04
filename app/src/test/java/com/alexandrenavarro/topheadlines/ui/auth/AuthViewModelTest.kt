package com.alexandrenavarro.topheadlines.ui.auth

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthViewModelTest {

    @Test
    fun `given biometric available when init then emits required state`() = runTest {
        val viewModel = AuthViewModel(FakeBiometricAuthenticator(canAuthenticate = true))

        viewModel.uiState.test {
            assertTrue(awaitItem() is AuthUiState.Required)
        }
    }

    @Test
    fun `given biometric not available when init then emits authenticated state`() = runTest {
        val viewModel = AuthViewModel(FakeBiometricAuthenticator(canAuthenticate = false))

        viewModel.uiState.test {
            assertTrue(awaitItem() is AuthUiState.Authenticated)
        }
    }

    @Test
    fun `given required state when authentication succeeds then emits authenticated state`() = runTest {
        val viewModel = AuthViewModel(FakeBiometricAuthenticator(canAuthenticate = true))

        viewModel.uiState.test {
            assertTrue(awaitItem() is AuthUiState.Required)

            viewModel.onAuthenticationSucceeded()

            assertTrue(awaitItem() is AuthUiState.Authenticated)
        }
    }

    @Test
    fun `given biometric available when isBiometricAvailable then returns true`() {
        val viewModel = AuthViewModel(FakeBiometricAuthenticator(canAuthenticate = true))

        assertTrue(viewModel.isBiometricAvailable())
    }

    @Test
    fun `given biometric not available when isBiometricAvailable then returns false`() {
        val viewModel = AuthViewModel(FakeBiometricAuthenticator(canAuthenticate = false))

        assertTrue(!viewModel.isBiometricAvailable())
    }
}

private class FakeBiometricAuthenticator(
    private val canAuthenticate: Boolean,
) : BiometricAuthenticator {
    override fun canAuthenticate(): Boolean = canAuthenticate
}
