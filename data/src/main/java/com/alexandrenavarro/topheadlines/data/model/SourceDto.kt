package com.alexandrenavarro.topheadlines.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SourceDto(
    val id: String?,
    val name: String?,
)
