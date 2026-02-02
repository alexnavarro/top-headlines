package com.alexandrenavarro.topheadlines.data.repository

import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.data.model.ArticleDto
import com.alexandrenavarro.topheadlines.data.model.SourceDto
import com.alexandrenavarro.topheadlines.data.remote.NewsRemoteDataSource
import com.alexandrenavarro.topheadlines.domain.model.Article
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class NewsRepositoryImplTest {

    private lateinit var repository: NewsRepositoryImpl
    private lateinit var fakeDataSource: FakeNewsRemoteDataSource

    @Before
    fun setUp() {
        fakeDataSource = FakeNewsRemoteDataSource()
        repository = NewsRepositoryImpl(fakeDataSource)
    }

    @Test
    fun `given datasource returns articles when getTopHeadlines then returns mapped domain articles`() = runTest {
        val articlesDto = listOf(
            ArticleDto(
                source = SourceDto(id = "bbc-news", name = "BBC News"),
                author = "John Doe",
                title = "Test Title",
                description = "Test Description",
                url = "https://example.com/article",
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2024-01-15T10:30:00Z",
                content = "Test Content",
            )
        )
        fakeDataSource.result = AppResult.Success(articlesDto)

        val result = repository.getTopHeadlines()

        assertTrue(result is AppResult.Success)
        val articles = (result as AppResult.Success).data
        assertEquals(1, articles.size)
        with(articles[0]) {
            assertEquals("Test Title", title)
            assertEquals("Test Description", description)
            assertEquals("https://example.com/article", url)
            assertEquals("https://example.com/image.jpg", imageUrl)
            assertEquals("2024-01-15T10:30:00Z", publishedAt)
            assertEquals("John Doe", author)
            assertEquals("BBC News", sourceName)
        }
    }

    @Test
    fun `given datasource returns empty list when getTopHeadlines then returns empty list`() = runTest {
        fakeDataSource.result = AppResult.Success(emptyList())

        val result = repository.getTopHeadlines()

        assertTrue(result is AppResult.Success)
        assertEquals(emptyList<Article>(), (result as AppResult.Success).data)
    }

    @Test
    fun `given datasource returns error when getTopHeadlines then returns error`() = runTest {
        val exception = IOException("Network error")
        fakeDataSource.result = AppResult.Error(exception)

        val result = repository.getTopHeadlines()

        assertTrue(result is AppResult.Error)
        assertEquals(exception, (result as AppResult.Error).exception)
    }

    @Test
    fun `given datasource returns article with null fields when getTopHeadlines then maps to empty strings`() = runTest {
        val articlesDto = listOf(
            ArticleDto(
                source = null,
                author = null,
                title = null,
                description = null,
                url = null,
                urlToImage = null,
                publishedAt = null,
                content = null,
            )
        )
        fakeDataSource.result = AppResult.Success(articlesDto)

        val result = repository.getTopHeadlines()

        assertTrue(result is AppResult.Success)
        val article = (result as AppResult.Success).data[0]
        assertEquals("", article.title)
        assertEquals("", article.description)
        assertEquals("", article.url)
        assertEquals(null, article.imageUrl)
        assertEquals("", article.publishedAt)
        assertEquals(null, article.author)
        assertEquals("", article.sourceName)
    }
}

private class FakeNewsRemoteDataSource : NewsRemoteDataSource(
    newsApiService = FakeNewsApiService(),
    source = "",
    apiKey = "",
) {
    var result: AppResult<List<ArticleDto>> = AppResult.Success(emptyList())

    override suspend fun getTopHeadlines(): AppResult<List<ArticleDto>> = result
}

private class FakeNewsApiService : com.alexandrenavarro.topheadlines.data.remote.NewsApiService {
    override suspend fun getTopHeadlines(sources: String, apiKey: String) =
        throw NotImplementedError("Not used in this test")
}
