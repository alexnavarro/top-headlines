package com.alexandrenavarro.topheadlines.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NewsSource

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NewsApiKey

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NewsProviderTitle

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IsDebug
