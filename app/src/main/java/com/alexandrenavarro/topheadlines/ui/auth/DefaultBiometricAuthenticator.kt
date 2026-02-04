package com.alexandrenavarro.topheadlines.ui.auth

import android.content.Context
import androidx.biometric.BiometricManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultBiometricAuthenticator @Inject constructor(
    @ApplicationContext private val context: Context,
) : BiometricAuthenticator {

    override fun canAuthenticate(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_WEAK
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }
}
