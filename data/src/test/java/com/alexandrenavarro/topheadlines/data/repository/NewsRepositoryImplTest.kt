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
            assertEquals("bbc-news_https://example.com/article", id)
            assertEquals("bbc-news", sourceId)
            assertEquals("Test Title", title)
            assertEquals("Test Description", description)
            assertEquals("https://example.com/article", url)
            assertEquals("https://example.com/image.jpg", imageUrl)
            assertEquals("2024-01-15T10:30:00Z", publishedAt)
            assertEquals("John Doe", author)
            assertEquals("BBC News", sourceName)
            assertEquals("Test Content", content)
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
    fun `given getTopHeadlines succeeded when getArticleById then returns cached article`() = runTest {
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
        repository.getTopHeadlines()

        val article = repository.getArticleById("bbc-news_https://example.com/article")

        assertEquals("Test Title", article?.title)
        assertEquals("https://example.com/article", article?.url)
    }

    @Test
    fun `given getTopHeadlines not called when getArticleById then returns null`() {
        val article = repository.getArticleById("bbc-news_https://example.com/article")

        assertEquals(null, article)
    }

    @Test
    fun `given getTopHeadlines returned error when getArticleById then returns null`() = runTest {
        fakeDataSource.result = AppResult.Error(IOException("Network error"))
        repository.getTopHeadlines()

        val article = repository.getArticleById("bbc-news_https://example.com/article")

        assertEquals(null, article)
    }

    @Test
    fun `given article with blank id when getTopHeadlines then does not cache it`() = runTest {
        val articlesDto = listOf(
            ArticleDto(
                source = null,
                author = null,
                title = "No URL",
                description = "Desc",
                url = null,
                urlToImage = null,
                publishedAt = "2024-01-15T10:30:00Z",
                content = null,
            )
        )
        fakeDataSource.result = AppResult.Success(articlesDto)
        repository.getTopHeadlines()

        val article = repository.getArticleById("")

        assertEquals(null, article)
    }

    @Test
    fun `given content with trailing char count when getTopHeadlines then strips it`() = runTest {
        val articlesDto = listOf(
            ArticleDto(
                source = SourceDto(id = "src", name = "Source"),
                author = null,
                title = "Title",
                description = "Desc",
                url = "https://example.com",
                urlToImage = null,
                publishedAt = "2024-01-15T10:30:00Z",
                content = "Some article text here [+222 chars]",
            )
        )
        fakeDataSource.result = AppResult.Success(articlesDto)

        val result = repository.getTopHeadlines()

        assertTrue(result is AppResult.Success)
        val article = (result as AppResult.Success).data[0]
        assertEquals("Some article text here", article.content)
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
        assertEquals("", article.id)
        assertEquals("", article.sourceId)
        assertEquals("", article.title)
        assertEquals("", article.description)
        assertEquals("", article.url)
        assertEquals(null, article.imageUrl)
        assertEquals("", article.publishedAt)
        assertEquals(null, article.author)
        assertEquals("", article.sourceName)
        assertEquals("", article.content)
    }
}

private class FakeNewsRemoteDataSource : NewsRemoteDataSource {
    var result: AppResult<List<ArticleDto>> = AppResult.Success(emptyList())

    override suspend fun getTopHeadlines(): AppResult<List<ArticleDto>> = result
}
