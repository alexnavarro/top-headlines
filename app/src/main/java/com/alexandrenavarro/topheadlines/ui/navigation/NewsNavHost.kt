package com.alexandrenavarro.topheadlines.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexandrenavarro.topheadlines.ui.detail.DetailScreen
import com.alexandrenavarro.topheadlines.ui.headlines.HeadlinesScreen

@Composable
fun NewsNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Route.Headlines,
        modifier = modifier,
    ) {
        composable<Route.Headlines> {
            HeadlinesScreen(
                onArticleClick = { articleId ->
                    navController.navigate(Route.Detail(articleId))
                }
            )
        }
        composable<Route.Detail> {
            DetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
