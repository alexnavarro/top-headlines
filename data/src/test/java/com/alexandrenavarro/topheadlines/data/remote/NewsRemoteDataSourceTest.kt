package com.alexandrenavarro.topheadlines.data.remote

import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.data.model.ArticleDto
import com.alexandrenavarro.topheadlines.data.model.SourceDto
import com.alexandrenavarro.topheadlines.data.model.TopHeadlinesResponseDto
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class NewsRemoteDataSourceTest {

    private lateinit var dataSource: NewsRemoteDataSourceImpl
    private lateinit var fakeNewsApiService: FakeNewsApiService

    private val source = "bbc-news"
    private val apiKey = "test-api-key"

    @Before
    fun setUp() {
        fakeNewsApiService = FakeNewsApiService()
        dataSource = NewsRemoteDataSourceImpl(
            newsApiService = fakeNewsApiService,
            source = source,
            apiKey = apiKey,
        )
    }

    @Test
    fun `given api returns articles when getTopHeadlines then returns success with articles`() = runTest {
        val articles = listOf(
            ArticleDto(
                source = SourceDto(id = "bbc-news", name = "BBC News"),
                author = "Author",
                title = "Title",
                description = "Description",
                url = "https://example.com",
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2024-01-01T00:00:00Z",
                content = "Content",
            )
        )
        fakeNewsApiService.response = TopHeadlinesResponseDto(
            status = "ok",
            totalResults = 1,
            articles = articles,
        )

        val result = dataSource.getTopHeadlines()

        assertTrue(result is AppResult.Success)
        assertEquals(articles, (result as AppResult.Success).data)
    }

    @Test
    fun `given api returns null articles when getTopHeadlines then returns success with empty list`() = runTest {
        fakeNewsApiService.response = TopHeadlinesResponseDto(
            status = "ok",
            totalResults = 0,
            articles = null,
        )

        val result = dataSource.getTopHeadlines()

        assertTrue(result is AppResult.Success)
        assertEquals(emptyList<ArticleDto>(), (result as AppResult.Success).data)
    }

    @Test
    fun `given api throws IOException when getTopHeadlines then returns error`() = runTest {
        val exception = IOException("Network error")
        fakeNewsApiService.exception = exception

        val result = dataSource.getTopHeadlines()

        assertTrue(result is AppResult.Error)
        assertEquals(exception, (result as AppResult.Error).exception)
    }

    @Test
    fun `given api throws HttpException when getTopHeadlines then returns error`() = runTest {
        val exception = HttpException(
            Response.error<TopHeadlinesResponseDto>(401, "Unauthorized".toResponseBody())
        )
        fakeNewsApiService.exception = exception

        val result = dataSource.getTopHeadlines()

        assertTrue(result is AppResult.Error)
        assertTrue((result as AppResult.Error).exception is HttpException)
    }

    @Test
    fun `given api call when getTopHeadlines then passes correct source and apiKey`() = runTest {
        fakeNewsApiService.response = TopHeadlinesResponseDto(
            status = "ok",
            totalResults = 0,
            articles = emptyList(),
        )

        dataSource.getTopHeadlines()

        assertEquals(source, fakeNewsApiService.lastSources)
        assertEquals(apiKey, fakeNewsApiService.lastApiKey)
    }
}

private class FakeNewsApiService : NewsApiService {
    var response: TopHeadlinesResponseDto? = null
    var exception: Exception? = null
    var lastSources: String? = null
    var lastApiKey: String? = null

    override suspend fun getTopHeadlines(sources: String, apiKey: String): TopHeadlinesResponseDto {
        lastSources = sources
        lastApiKey = apiKey
        exception?.let { throw it }
        return response ?: throw IllegalStateException("No response configured")
    }
}
