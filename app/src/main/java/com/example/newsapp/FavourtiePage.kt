package com.example.newsapp

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newsapp.viewModel.NewsViewModel

@Composable
fun FavouritePage(newsViewModel: NewsViewModel, navController: NavController, onNavigate: (@Composable () -> Unit) -> Unit) {
    val favouriteNews = newsViewModel.favourites // State of the favourites
    Column {
        Text(text = "Favourite News", modifier = Modifier.padding(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            items(favouriteNews.value ?: emptyList()) { article ->
                ArticleItem(
                    article = article,
                    onClick = {
                        navController.navigate("newsArticle/${Uri.encode(article.url)}")
                    },
                    onFavoriteToggle = {
                        newsViewModel.toggleFavorite(article)
                    },
                    navController = navController
                )
            }
        }
    }
}

