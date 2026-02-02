package com.alexandrenavarro.topheadlines.domain.usecase

import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.domain.model.Article
import com.alexandrenavarro.topheadlines.domain.repository.NewsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetTopHeadlinesUseCaseTest {

    private lateinit var useCase: GetTopHeadlinesUseCase
    private lateinit var fakeRepository: FakeNewsRepository

    @Before
    fun setUp() {
        fakeRepository = FakeNewsRepository()
        useCase = GetTopHeadlinesUseCase(fakeRepository)
    }

    @Test
    fun `given repository returns articles when invoke then returns articles sorted by publishedAt desc`() = runTest {
        val articles = listOf(
            createArticle(title = "Old", publishedAt = "2024-01-01T10:00:00Z"),
            createArticle(title = "New", publishedAt = "2024-01-15T10:00:00Z"),
            createArticle(title = "Middle", publishedAt = "2024-01-10T10:00:00Z"),
        )
        fakeRepository.result = AppResult.Success(articles)

        val result = useCase()

        assertTrue(result is AppResult.Success)
        val sorted = (result as AppResult.Success).data
        assertEquals("New", sorted[0].title)
        assertEquals("Middle", sorted[1].title)
        assertEquals("Old", sorted[2].title)
    }

    @Test
    fun `given articles with same date when invoke then maintains stable order`() = runTest {
        val articles = listOf(
            createArticle(title = "First", publishedAt = "2024-01-15T10:00:00Z"),
            createArticle(title = "Second", publishedAt = "2024-01-15T10:00:00Z"),
        )
        fakeRepository.result = AppResult.Success(articles)

        val result = useCase()

        assertTrue(result is AppResult.Success)
        val sorted = (result as AppResult.Success).data
        assertEquals("First", sorted[0].title)
        assertEquals("Second", sorted[1].title)
    }

    @Test
    fun `given article with empty publishedAt when invoke then pushes to end`() = runTest {
        val articles = listOf(
            createArticle(title = "Empty Date", publishedAt = ""),
            createArticle(title = "Valid Date", publishedAt = "2024-01-15T10:00:00Z"),
        )
        fakeRepository.result = AppResult.Success(articles)

        val result = useCase()

        assertTrue(result is AppResult.Success)
        val sorted = (result as AppResult.Success).data
        assertEquals("Valid Date", sorted[0].title)
        assertEquals("Empty Date", sorted[1].title)
    }

    @Test
    fun `given article with invalid date format when invoke then pushes to end`() = runTest {
        val articles = listOf(
            createArticle(title = "Invalid Date", publishedAt = "invalid-date"),
            createArticle(title = "Valid Date", publishedAt = "2024-01-15T10:00:00Z"),
        )
        fakeRepository.result = AppResult.Success(articles)

        val result = useCase()

        assertTrue(result is AppResult.Success)
        val sorted = (result as AppResult.Success).data
        assertEquals("Valid Date", sorted[0].title)
        assertEquals("Invalid Date", sorted[1].title)
    }

    @Test
    fun `given multiple invalid dates when invoke then groups them at end`() = runTest {
        val articles = listOf(
            createArticle(title = "Empty", publishedAt = ""),
            createArticle(title = "Valid Old", publishedAt = "2024-01-01T10:00:00Z"),
            createArticle(title = "Invalid", publishedAt = "not-a-date"),
            createArticle(title = "Valid New", publishedAt = "2024-01-15T10:00:00Z"),
            createArticle(title = "Blank", publishedAt = "   "),
        )
        fakeRepository.result = AppResult.Success(articles)

        val result = useCase()

        assertTrue(result is AppResult.Success)
        val sorted = (result as AppResult.Success).data
        assertEquals("Valid New", sorted[0].title)
        assertEquals("Valid Old", sorted[1].title)
        // Invalid dates at the end (order among them is stable)
        assertEquals("Empty", sorted[2].title)
        assertEquals("Invalid", sorted[3].title)
        assertEquals("Blank", sorted[4].title)
    }

    @Test
    fun `given repository returns empty list when invoke then returns empty list`() = runTest {
        fakeRepository.result = AppResult.Success(emptyList())

        val result = useCase()

        assertTrue(result is AppResult.Success)
        assertEquals(emptyList<Article>(), (result as AppResult.Success).data)
    }

    @Test
    fun `given repository returns error when invoke then returns error`() = runTest {
        val exception = IOException("Network error")
        fakeRepository.result = AppResult.Error(exception)

        val result = useCase()

        assertTrue(result is AppResult.Error)
        assertEquals(exception, (result as AppResult.Error).exception)
    }

    private fun createArticle(
        title: String,
        publishedAt: String,
    ) = Article(
        title = title,
        description = "Description",
        url = "https://example.com",
        imageUrl = null,
        publishedAt = publishedAt,
        author = null,
        sourceName = "Source",
    )
}

private class FakeNewsRepository : NewsRepository {
    var result: AppResult<List<Article>> = AppResult.Success(emptyList())

    override suspend fun getTopHeadlines(): AppResult<List<Article>> = result
}
