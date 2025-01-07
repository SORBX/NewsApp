package com.example.newsapp

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newsapp.viewModel.NewsViewModel

@SuppressLint("MutableCollectionMutableState")
@Composable
fun RecentNewsScreen(
    viewModel: NewsViewModel,
    navController: NavController,
    onNavigate: (@Composable () -> Unit) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }
    val recentArticles by viewModel.recent.observeAsState(emptyList())
    val filteredArticles = if (searchQuery.isEmpty()) {
        recentArticles
    } else {
        recentArticles.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.description?.contains(searchQuery, ignoreCase = true) == true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Clear History Button
        Button(
            onClick = { viewModel.clearRecent() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Clear History")
        }

        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSearchExpanded) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(48.dp)
                        .border(1.dp, Color.Gray, CircleShape)
                        .clip(CircleShape),
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    trailingIcon = {
                        Row {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear search",
                                        tint = Color.Gray
                                    )
                                }
                            }
                            IconButton(onClick = { isSearchExpanded = false }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search icon"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { isSearchExpanded = false })
                )
            } else {
                IconButton(onClick = { isSearchExpanded = true }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
                }
            }
        }

        // Display recent articles or "No recent articles" message
        if (filteredArticles.isEmpty()) {
            Text(
                text = "No recent articles",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredArticles) { article ->
                    ArticleItem(
                        article = article,
                        navController = navController,
                        onClick = {
                            viewModel.markArticleAsAccessed(article)
                            navController.navigate("newsArticle/${Uri.encode(article.url)}")
                        },
                        onFavoriteToggle = {
                            viewModel.toggleFavorite(article)
                        }
                    )
                }
            }
        }
    }
}
