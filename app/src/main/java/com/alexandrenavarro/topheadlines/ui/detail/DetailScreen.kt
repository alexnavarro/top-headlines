package com.alexandrenavarro.topheadlines.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.alexandrenavarro.topheadlines.domain.model.Article
import com.alexandrenavarro.topheadlines.ui.theme.TopHeadlinesTheme

@Composable
fun DetailScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailContent(
        uiState = uiState,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailContent(
    uiState: DetailUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        when (uiState) {
            is DetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is DetailUiState.Success -> {
                ArticleDetail(
                    article = uiState.article,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                )
            }

            is DetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticleDetail(
    article: Article,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        ArticleHeaderImage(imageUrl = article.imageUrl)
        Column(modifier = Modifier.padding(16.dp)) {
            ArticleTitle(title = article.title)
            Spacer(modifier = Modifier.height(12.dp))
            ArticleDescription(description = article.description)
            Spacer(modifier = Modifier.height(16.dp))
            ArticleContent(content = article.content)
        }
    }
}

@Composable
private fun ArticleHeaderImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun ArticleTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier,
    )
}

@Composable
private fun ArticleDescription(
    description: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}

@Composable
private fun ArticleContent(
    content: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = content,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailContentSuccessPreview() {
    TopHeadlinesTheme {
        DetailContent(
            uiState = DetailUiState.Success(
                article = Article(
                    id = "1",
                    title = "Breaking: Major Discovery in Space Exploration",
                    description = "Scientists have announced a groundbreaking finding that could reshape our understanding of the universe and open new possibilities for future missions.",
                    url = "https://example.com/article",
                    imageUrl = "https://example.com/image.jpg",
                    publishedAt = "2024-01-15T10:30:00Z",
                    author = "Jane Smith",
                    sourceName = "BBC News",
                    content = "In a landmark announcement today, researchers at the International Space Agency revealed evidence of organic compounds on a distant exoplanet. The discovery, made possible by the latest generation of space telescopes, marks the first time such compounds have been detected outside our solar system.\n\nThe findings, published in the journal Nature, suggest that the conditions necessary for life may be more common in the universe than previously thought. \"This is a pivotal moment in our search for extraterrestrial life,\" said Dr. Sarah Chen, lead researcher on the project.\n\nThe team used spectroscopic analysis to identify the chemical signatures of amino acids in the planet's atmosphere, a technique that had been theorized but never successfully applied at such distances.",
                ),
            ),
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailContentSuccessNoImagePreview() {
    TopHeadlinesTheme {
        DetailContent(
            uiState = DetailUiState.Success(
                article = Article(
                    id = "2",
                    title = "Technology Update: New Framework Released",
                    description = "A new open-source framework promises to simplify mobile development across platforms.",
                    url = "https://example.com/article2",
                    imageUrl = null,
                    publishedAt = "2024-01-15T10:30:00Z",
                    author = null,
                    sourceName = "Tech News",
                    content = "The open-source community has released a new framework that aims to unify mobile development. Early benchmarks show significant performance improvements over existing solutions.",
                ),
            ),
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailContentLoadingPreview() {
    TopHeadlinesTheme {
        DetailContent(
            uiState = DetailUiState.Loading,
            onBackClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailContentErrorPreview() {
    TopHeadlinesTheme {
        DetailContent(
            uiState = DetailUiState.Error("Article not found"),
            onBackClick = {},
        )
    }
}
