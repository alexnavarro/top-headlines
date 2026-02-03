package com.alexandrenavarro.topheadlines.domain.usecase

import com.alexandrenavarro.topheadlines.core.result.AppResult
import com.alexandrenavarro.topheadlines.domain.model.Article
import com.alexandrenavarro.topheadlines.domain.repository.NewsRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetArticleByIdUseCaseTest {

    private lateinit var useCase: GetArticleByIdUseCase
    private lateinit var fakeRepository: FakeNewsRepositoryForArticleById

    @Before
    fun setUp() {
        fakeRepository = FakeNewsRepositoryForArticleById()
        useCase = GetArticleByIdUseCase(fakeRepository)
    }

    @Test
    fun `given repository has article when invoke with url then returns article`() {
        val article = createArticle(url = "https://example.com/article")
        fakeRepository.articleById = article

        val result = useCase("https://example.com/article")

        assertEquals(article, result)
    }

    @Test
    fun `given repository has no article when invoke with url then returns null`() {
        fakeRepository.articleById = null

        val result = useCase("https://example.com/unknown")

        assertNull(result)
    }

    private fun createArticle(url: String) = Article(
        id = url,
        title = "Title",
        description = "Description",
        url = url,
        imageUrl = null,
        publishedAt = "2024-01-15T10:00:00Z",
        author = null,
        sourceName = "Source",
        content = "Content",
    )
}

private class FakeNewsRepositoryForArticleById : NewsRepository {
    var articleById: Article? = null

    override suspend fun getTopHeadlines(): AppResult<List<Article>> =
        AppResult.Success(emptyList())

    override fun getArticleById(id: String): Article? = articleById
}
