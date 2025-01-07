package com.example.newsapp

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.newsapp.viewModel.NewsViewModel

@Composable
fun FooterBar(
    navController: androidx.navigation.NavHostController,
    newsViewModel: NewsViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            label = { Text("Home") },
            icon = { Icon(painter = painterResource(id = R.drawable.baseline_home_24), contentDescription = "Home") }
        )
        NavigationBarItem(
            selected = currentRoute == "recent",
            onClick = { navController.navigate("recent") },
            label = { Text("Recent") },
            icon = { Icon(painter = painterResource(id = R.drawable.baseline_history_24), contentDescription = "Recent") }
        )
        NavigationBarItem(
            selected = currentRoute == "favourites",
            onClick = { navController.navigate("favourites") },
            label = { Text("Favourites") },
            icon = { Icon(painter = painterResource(id = R.drawable.baseline_favorite_24), contentDescription = "Favourites") }
        )
        NavigationBarItem(
            selected = currentRoute == "weather",
            onClick = { navController.navigate("weather") },
            label = { Text("Weather") },
            icon = { Icon(painter = painterResource(id = R.drawable.baseline_sunny_24), contentDescription = "Weather") }
        )
        NavigationBarItem(
            selected = currentRoute == "info",
            onClick = { navController.navigate("info") },
            label = { Text("Info") },
            icon = { Icon(painter = painterResource(id = R.drawable.baseline_info_outline_24), contentDescription = "Info") }
        )
    }
}



