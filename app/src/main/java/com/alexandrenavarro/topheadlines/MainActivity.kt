package com.alexandrenavarro.topheadlines

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexandrenavarro.topheadlines.ui.auth.AuthLockedScreen
import com.alexandrenavarro.topheadlines.ui.auth.AuthUiState
import com.alexandrenavarro.topheadlines.ui.auth.AuthViewModel
import com.alexandrenavarro.topheadlines.ui.navigation.NewsNavHost
import com.alexandrenavarro.topheadlines.ui.theme.TopHeadlinesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (authViewModel.isBiometricAvailable()) {
            showBiometricPrompt()
        }

        setContent {
            TopHeadlinesTheme {
                val authState by authViewModel.uiState.collectAsStateWithLifecycle()

                when (authState) {
                    is AuthUiState.Authenticated -> NewsNavHost()
                    is AuthUiState.Required -> AuthLockedScreen(
                        onRetryClick = ::showBiometricPrompt,
                    )
                }
            }
        }
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                authViewModel.onAuthenticationSucceeded()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            }

            override fun onAuthenticationFailed() {
            }
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.auth_title))
            .setSubtitle(getString(R.string.biometric_prompt_subtitle))
            .setNegativeButtonText(getString(R.string.biometric_prompt_cancel))
            .build()

        BiometricPrompt(this, executor, callback).authenticate(promptInfo)
    }
}
