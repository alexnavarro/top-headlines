package com.alexandrenavarro.topheadlines.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Headlines : Route

    @Serializable
    data class Detail(val articleUrl: String) : Route
}
