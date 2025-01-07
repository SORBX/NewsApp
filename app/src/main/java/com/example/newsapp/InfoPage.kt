package com.example.newsapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun InfoPage(navController: NavHostController, onNavigate: (@Composable () -> Unit) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "About the App",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "This is a news application developed with Jetpack Compose. This app is developed by computer science students who use Android Studio using Kotlin as their coding program.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Benefits of using Jetpack Compose and Kotlin for Android Development:",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "- Declarative UI: Compose simplifies UI development with its declarative approach, making it easier to build complex UIs and maintain code.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "- Kotlin Interoperability: Seamlessly integrate Kotlin with Java libraries and frameworks.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "- Performance: Compose leverages modern rendering techniques for smooth and efficient UI performance.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "- Modern Language: Kotlin is a concise and expressive language with features like null safety and coroutines, improving developer productivity and code quality.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Version: 1.0.0", style = MaterialTheme.typography.bodyMedium)
    }
}
