package com.example.newsapp




import NewsArticlePage
import WeatherPage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.viewModel.NewsViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {

    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NewsApp(newsViewModel = newsViewModel, navController = navController)
        }
    }
}



@Composable
fun NewsApp(newsViewModel: NewsViewModel, navController: androidx.navigation.NavHostController) {
    val article = newsViewModel.articles.observeAsState(emptyList())

    NewsAppTheme {
        Scaffold(
            bottomBar = { FooterBar(navController = navController, newsViewModel = newsViewModel) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Text(
                    text = "NEWS",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Black,
                    fontSize = 25.sp,
                    fontFamily = FontFamily.Serif
                )
                NavHost(navController, startDestination = "home") {
                    composable("home") {
                        HomePage(newsViewModel = newsViewModel, navController = navController, onNavigate = {})
                    }
                    composable("recent") {
                        RecentNewsScreen(viewModel = newsViewModel, navController = navController, onNavigate = {})
                    }
                    composable(
                        route = "newsArticle/{url}",
                        arguments = listOf(navArgument("url") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val url = backStackEntry.arguments?.getString("url")?.let {
                            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                        } ?: ""
                        val currentArticle = newsViewModel.articles.value?.find { it.url == url }

                        NewsArticlePage(
                            url = url,
                            isFavorite = currentArticle?.isFavourite ?: false,
                            onFavoriteToggle = { isFavorite ->
                                currentArticle?.let {
                                    newsViewModel.toggleFavorite(it.copy(isFavourite = isFavorite))
                                }
                            }
                        )
                    }

                    composable("favourites") { FavouritePage(newsViewModel, navController, onNavigate = {}) }
                    composable("weather") { WeatherPage(navController, onNavigate = {}) }
                    composable("info") { InfoPage(navController, onNavigate = {}) }
                }
            }
        }
    }
}
