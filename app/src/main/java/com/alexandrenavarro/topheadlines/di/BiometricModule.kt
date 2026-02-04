package com.alexandrenavarro.topheadlines.di

import com.alexandrenavarro.topheadlines.ui.auth.BiometricAuthenticator
import com.alexandrenavarro.topheadlines.ui.auth.DefaultBiometricAuthenticator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BiometricModule {

    @Binds
    abstract fun bindBiometricAuthenticator(
        impl: DefaultBiometricAuthenticator,
    ): BiometricAuthenticator
}
