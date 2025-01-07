package com.example.newsapp

import kotlinx.serialization.Serializable


@Serializable
object HomePageScreen

@Serializable
object RecentNewsScreen

@Serializable
data class NewsArticleScreen(
    val url : String
)