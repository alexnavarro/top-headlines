package com.alexandrenavarro.topheadlines.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.alexandrenavarro.topheadlines.ui.detail.DetailScreen
import com.alexandrenavarro.topheadlines.ui.headlines.HeadlinesScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                onArticleClick = { articleUrl ->
                    val encodedUrl = URLEncoder.encode(articleUrl, StandardCharsets.UTF_8.toString())
                    navController.navigate(Route.Detail(encodedUrl))
                }
            )
        }
        composable<Route.Detail> { backStackEntry ->
            val route: Route.Detail = backStackEntry.toRoute()
            val decodedUrl = URLDecoder.decode(route.articleUrl, StandardCharsets.UTF_8.toString())
            DetailScreen(
                articleUrl = decodedUrl,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
