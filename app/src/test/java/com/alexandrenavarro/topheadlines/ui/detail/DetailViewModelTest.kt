package com.alexandrenavarro.topheadlines.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.domain.model.Article
import com.alexandrenavarro.topheadlines.domain.repository.NewsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DetailViewModelTest {

    @Test
    fun `given article exists when init then emits success state`() = runTest {
        val article = createArticle()
        val viewModel = createViewModel(
            articleId = "bbc-news_https://example.com/article",
            article = article,
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is DetailUiState.Success)
            assertEquals(article, (state as DetailUiState.Success).article)
        }
    }

    @Test
    fun `given article not found when init then emits error state`() = runTest {
        val viewModel = createViewModel(
            articleId = "bbc-news_https://example.com/unknown",
            article = null,
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is DetailUiState.Error)
            assertEquals("Article not found", (state as DetailUiState.Error).message)
        }
    }

    private fun createViewModel(
        articleId: String,
        article: Article?,
    ): DetailViewModel {
        val fakeRepository = FakeNewsRepository(article)
        val savedStateHandle = SavedStateHandle(mapOf("articleId" to articleId))
        return DetailViewModel(savedStateHandle, fakeRepository)
    }

    private fun createArticle() = Article(
        id = "bbc-news_https://example.com/article",
        sourceId = "bbc-news",
        title = "Test Title",
        description = "Test Description",
        url = "https://example.com/article",
        imageUrl = "https://example.com/image.jpg",
        publishedAt = "2024-01-15T10:00:00Z",
        author = "John Doe",
        sourceName = "BBC News",
        content = "Test Content",
    )
}

private class FakeNewsRepository(
    private val article: Article?,
) : NewsRepository {

    override suspend fun getTopHeadlines(): AppResult<List<Article>> =
        AppResult.Success(emptyList())

    override fun getArticleById(id: String): Article? = article
}
