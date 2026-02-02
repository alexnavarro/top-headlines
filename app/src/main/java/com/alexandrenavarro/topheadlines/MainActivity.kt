package com.alexandrenavarro.topheadlines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alexandrenavarro.topheadlines.ui.navigation.NewsNavHost
import com.alexandrenavarro.topheadlines.ui.theme.TopHeadlinesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TopHeadlinesTheme {
                NewsNavHost()
            }
        }
    }
}
