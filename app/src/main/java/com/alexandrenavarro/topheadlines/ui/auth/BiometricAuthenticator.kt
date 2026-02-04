package com.alexandrenavarro.topheadlines.ui.auth

interface BiometricAuthenticator {
    fun canAuthenticate(): Boolean
}
