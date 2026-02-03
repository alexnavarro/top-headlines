package com.alexandrenavarro.topheadlines.ui.headlines

import app.cash.turbine.test
import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.domain.model.Article
import com.alexandrenavarro.topheadlines.domain.usecase.GetTopHeadlinesUseCase
import com.alexandrenavarro.topheadlines.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class HeadlinesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given usecase returns articles when init then emits success state`() = runTest {
        val articles = listOf(
            createArticle(title = "Article 1"),
            createArticle(title = "Article 2"),
        )
        val viewModel = createViewModel(AppResult.Success(articles))

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is HeadlinesUiState.Success)
            assertEquals(articles, (state as HeadlinesUiState.Success).articles)
        }
    }

    @Test
    fun `given usecase returns empty list when init then emits success with empty list`() = runTest {
        val viewModel = createViewModel(AppResult.Success(emptyList()))

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is HeadlinesUiState.Success)
            assertEquals(emptyList<Article>(), (state as HeadlinesUiState.Success).articles)
        }
    }

    @Test
    fun `given usecase returns error when init then emits error state`() = runTest {
        val viewModel = createViewModel(AppResult.Error(IOException("Network error")))

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is HeadlinesUiState.Error)
            assertEquals("Network error", (state as HeadlinesUiState.Error).message)
        }
    }

    @Test
    fun `given usecase returns error with null message when init then emits unknown error`() = runTest {
        val viewModel = createViewModel(AppResult.Error(IOException()))

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is HeadlinesUiState.Error)
            assertEquals("Unknown error", (state as HeadlinesUiState.Error).message)
        }
    }

    @Test
    fun `given error state when loadHeadlines succeeds then emits success state`() = runTest {
        val fakeRepository = FakeNewsRepository()
        fakeRepository.result = AppResult.Error(IOException("Network error"))
        val useCase = GetTopHeadlinesUseCase(fakeRepository)
        val viewModel = HeadlinesViewModel(useCase)

        assertTrue(viewModel.uiState.value is HeadlinesUiState.Error)

        val articles = listOf(createArticle(title = "Recovered"))
        fakeRepository.result = AppResult.Success(articles)
        viewModel.loadHeadlines()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is HeadlinesUiState.Success)
            assertEquals(articles, (state as HeadlinesUiState.Success).articles)
        }
    }

    private fun createViewModel(result: AppResult<List<Article>>): HeadlinesViewModel {
        val fakeRepository = FakeNewsRepository().apply { this.result = result }
        return HeadlinesViewModel(GetTopHeadlinesUseCase(fakeRepository))
    }

    private fun createArticle(title: String) = Article(
        title = title,
        description = "Description",
        url = "https://example.com",
        imageUrl = null,
        publishedAt = "2024-01-15T10:00:00Z",
        author = null,
        sourceName = "Source",
    )
}

private class FakeNewsRepository : NewsRepository {
    var result: AppResult<List<Article>> = AppResult.Success(emptyList())

    override suspend fun getTopHeadlines(): AppResult<List<Article>> = result
}
