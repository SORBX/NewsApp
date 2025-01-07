package com.example.newsapp.weather

data class WeatherResponse(
    val main: Main,
    val weather: List<WeatherDescription>,
    val name: String // City name
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val humidity: Int
)

data class WeatherDescription(
    val description: String
)
