package com.example.weatherapplication

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var weatherTextView: TextView
    private val apiKey = "b72404d39e788f238613f622dfddb33a" //My Api Key - Update to users key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTextView = findViewById(R.id.weatherTextView)

        // Run method to fetch and update ui to reflect current weather in a city
        getWeatherData("Nelson")
    }

    private fun getWeatherData(cityName: String) {
        // Retrofit instance for converting
        val retrofit = ApiClient.retrofit
        val service = retrofit.create(WeatherApiService::class.java)

        // Make API call to OpenWeather
        val call = service.getWeather(cityName, apiKey, "metric")

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    val weatherInfo = """
                        City: ${weatherResponse?.name}
                        Temperature: ${weatherResponse?.main?.temp}°C
                        Humidity: ${weatherResponse?.main?.humidity}%
                        Description: ${weatherResponse?.weather?.get(0)?.description}
                        Feels Like: ${weatherResponse?.main?.feels_like}°C
                        Min Temp: ${weatherResponse?.main?.temp_min}°C
                        Max Temp: ${weatherResponse?.main?.temp_max}°C
                    """
                    weatherTextView.text = weatherInfo
                } else {
                    Toast.makeText(this@MainActivity, "Failed to get weather data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
