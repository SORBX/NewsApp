package com.example.newsapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.Constant
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.request.EverythingRequest
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import java.util.*


class NewsViewModel : ViewModel() {

    private val _article = MutableLiveData<List<ArticleNews>>()
    val articles: MutableLiveData<List<ArticleNews>> = _article

    private val _favourites = MutableLiveData<List<ArticleNews>>()
    val favourites: LiveData<List<ArticleNews>> = _favourites

    private val _recent = MutableLiveData<List<ArticleNews>>()
    val recent: LiveData<List<ArticleNews>> = _recent


    init {
        fetchNewsTopHeadlines()
    }



    fun fetchNewsTopHeadlines(category: String = "GENERAL") {
        val newsApiClient = NewsApiClient(Constant.apiKey)

        val request = TopHeadlinesRequest.Builder().language("en").category(category).build()

        newsApiClient.getTopHeadlines(request, object : NewsApiClient.ArticlesResponseCallback {
            override fun onSuccess(response: ArticleResponse?) {
                response?.articles?.let { articleList ->
                    // Map the fetched articles to ArticleItem objects
                    val articleItems = articleList.map { article ->
                        ArticleNews(
                            title = article.title ?: "No Title",
                            description = article.description ?: "No Description",
                            imageUrl = article.urlToImage ?: "",
                            url = article.url ?: "",
                            sourceName = article.source?.name ?: "Unknown Source"// Map source.name
                        )
                    }

                    articles.postValue(articleItems) // Post the transformed list
                }
            }

            override fun onFailure(throwable: Throwable?) {
                throwable?.localizedMessage?.let { Log.e("NewsAPI Response Failed", it) }
            }
        })
    }

    fun fetchEverythingWithQuery(query: String) {
        val newsApiClient = NewsApiClient(Constant.apiKey)

        val request = EverythingRequest.Builder().language("en").q(query).build()

        newsApiClient.getEverything(request, object : NewsApiClient.ArticlesResponseCallback {
            override fun onSuccess(response: ArticleResponse?) {
                response?.articles?.let { articleList ->
                    // Map the fetched articles to ArticleItem objects
                    val articleItems = articleList.map { article ->
                        ArticleNews(
                            title = article.title ?: "No Title",
                            description = article.description ?: "No Description",
                            imageUrl = article.urlToImage ?: "",
                            url = article.url ?: "",
                            sourceName = article.source?.name ?: "Unknown Source" // Map source.name
                        )
                    }

                    articles.postValue(articleItems) // Post the transformed list
                }
            }

            override fun onFailure(throwable: Throwable?) {
                throwable?.localizedMessage?.let { Log.e("NewsAPI Response Failed", it) }
            }
        })
    }

    fun addToRecent(article: ArticleNews) {
        Log.d("NewsViewModel", "Adding to recent: $article")
        val currentList = _recent.value ?: emptyList()
        // Avoid duplication
        if (!currentList.contains(article)) {
            _recent.value = currentList + article
            Log.d("NewsViewModel", "Recent Articles Updated: ${_recent.value}")
        } else {
            Log.d("NewsViewModel", "Article already in recent: ${article.url}")
        }
    }

    fun clearRecent() {
        _recent.value = emptyList()
    }



    fun toggleFavorite(article: ArticleNews) {
        // Find the article in the current list and update its favorite status
        val updatedArticles = _article.value?.map {
            if (it == article) {
                it.copy(isFavourite = !it.isFavourite) // Create a new copy with updated status
            } else {
                it // Keep other articles unchanged
            }
        } ?: emptyList()

        // Post the updated list to _articles
        _article.postValue(updatedArticles)

        // Filter and update favorites
        _favourites.postValue(updatedArticles.filter { it.isFavourite })
    }

    fun getAccessedArticlesGroupedByDate(): Map<String, List<ArticleNews>> {
        val groupedArticles = getAccessedArticles().groupBy {
            when {
                it.accessTimestamp >= getStartOfDay(System.currentTimeMillis()) -> "Today"
                it.accessTimestamp >= getStartOfDay(System.currentTimeMillis() - 24 * 60 * 60 * 1000) -> "Yesterday"
                it.accessTimestamp >= getStartOfDay(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000) -> "Past 7 Days"
                else -> "Older"
            }
        }
        Log.d("NewsViewModel", "Grouped Articles: ${groupedArticles.toString()}")
        return groupedArticles
    }


    private fun getStartOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    fun markArticleAsAccessed(article: ArticleNews) {
        Log.d("NewsViewModel", "Before update: ${article.url} - ${article.accessTimestamp}")

        _article.value?.let { currentArticles ->
            val updatedArticles = currentArticles.map { currentArticle ->
                if (currentArticle.url == article.url) {
                    currentArticle.copy(accessTimestamp = System.currentTimeMillis())
                } else {
                    currentArticle
                }
            }
            _article.postValue(updatedArticles)
        }

        Log.d("NewsViewModel", "After update: ${article.url} - ${article.accessTimestamp}")
    }

    private fun getAccessedArticles(): List<ArticleNews> {
        Log.d("NewsViewModel", "Getting accessed articles...")
        val accessedArticles = _article.value?.filter { it.accessTimestamp > 0 } ?: emptyList()
        Log.d("NewsViewModel", "Accessed articles: ${accessedArticles.size}")
        return accessedArticles
    }



}

data class ArticleNews(
    val title: String,
    val description: String,
    val isFavourite: Boolean = false,
    val url: String,
    val imageUrl: String,
    val sourceName: String,
    val accessTimestamp: Long = System.currentTimeMillis(),
    var isAccessed: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        return other is ArticleNews && this.url == other.url
    }

    override fun hashCode(): Int {
        return url.hashCode()
    }
}

