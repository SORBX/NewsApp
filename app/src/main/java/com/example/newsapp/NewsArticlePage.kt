import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun NewsArticlePage(
    url: String,
    isFavorite: Boolean,
    onFavoriteToggle: (Boolean) -> Unit
) {
    val isCurrentlyFavorite = remember { mutableStateOf(isFavorite) }

    Box(modifier = Modifier.fillMaxSize()) {
        // WebView for displaying the article
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    loadUrl(url)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // FloatingActionButton for toggling favorite
        FloatingActionButton(
            onClick = {
                isCurrentlyFavorite.value = !isCurrentlyFavorite.value
                onFavoriteToggle(isCurrentlyFavorite.value)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = if (isCurrentlyFavorite.value) {Icons.Filled.Favorite} else {Icons.Outlined.FavoriteBorder},
                contentDescription = "Toggle Favorite"
            )
        }
    }
}
