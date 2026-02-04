package com.alexandrenavarro.topheadlines.ui.auth

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
