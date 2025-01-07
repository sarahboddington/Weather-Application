package com.example.weatherapplication

data class WeatherResponse(
    val main: Main,
    val name: String,
    val weather: List<Weather>
)

data class Main(
    val temp: Float,
    val temp_min: Float,
    val temp_max: Float,
    val feels_like: Float,
    val humidity: Int
)

data class Weather(
    val description: String
)
