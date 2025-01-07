package com.example.weatherapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    fun getWeather(
        @Query("q") cityName: String,           // City name
        @Query("appid") apiKey: String,         // API key
        @Query("units") units: String          // Units (Default is Kelvin so have to be explicit to ensure metric)
    ): Call<WeatherResponse>
}