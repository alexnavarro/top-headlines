package com.alexandrenavarro.topheadlines.data.remote

import com.alexandrenavarro.topheadlines.data.model.TopHeadlinesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("sources") sources: String,
        @Query("apiKey") apiKey: String,
    ): TopHeadlinesResponseDto
}
