package com.example.weatherapplication

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var cityTextView: TextView
    private lateinit var tempTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var minTextView: TextView
    private lateinit var maxTextView: TextView
    private lateinit var humidityTextView: TextView
    private lateinit var windTextView: TextView
    private lateinit var currentSymbol: ImageView

    private val apiKey = "2a5ac747a9ba6b1731794920819dd9dd" //My Api Key - Update to users key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cityTextView = findViewById(R.id.currentCity)
        tempTextView = findViewById(R.id.currentTemp)
        descriptionTextView = findViewById(R.id.currentDescription)
        minTextView = findViewById(R.id.currentMin)
        maxTextView = findViewById(R.id.currentMax)
        humidityTextView = findViewById(R.id.currentHumidity)
        windTextView = findViewById(R.id.currentWind)
        currentSymbol = findViewById(R.id.currentSymbol)
        // Run method to fetch and update ui to reflect current weather in a city
        getWeatherData("Wellington")
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
                    cityTextView.text = cityName
                    tempTextView.text ="${weatherResponse?.main?.temp}°C "
                    descriptionTextView.text = "${weatherResponse?.weather?.get(0)?.description}"
                    minTextView.text = "${weatherResponse?.main?.temp_min}°C"
                    maxTextView.text = "${weatherResponse?.main?.temp_max}°C"
                    humidityTextView.text = "${weatherResponse?.main?.humidity}%"
                    windTextView.text = "${weatherResponse?.weather?.get(0)?.icon}"
                    val symbolString = "${weatherResponse?.weather?.get(0)?.icon}"
                    val symbolID = when (symbolString) {
                        "01d" -> R.drawable.icon_01d
                        "01n" -> R.drawable.icon_01n
                        "02d" -> R.drawable.icon_02d
                        "02n" -> R.drawable.icon_02n
                        "03d" -> R.drawable.icon_03d
                        "03n" -> R.drawable.icon_03n
                        "04d" -> R.drawable.icon_04d
                        "04n" -> R.drawable.icon_04n
                        "09d" -> R.drawable.icon_09d
                        "09n" -> R.drawable.icon_09n
                        "10d" -> R.drawable.icon_10d
                        "10n" -> R.drawable.icon_10n
                        "11d" -> R.drawable.icon_11d
                        "11n" -> R.drawable.icon_11n
                        "13d" -> R.drawable.icon_13d
                        "13n" -> R.drawable.icon_13n
                        "50d" -> R.drawable.icon_50d
                        "50n" -> R.drawable.icon_50n
                        else -> R.drawable.icon_default
                    }
                    val drawable = ContextCompat.getDrawable(this@MainActivity, symbolID)
                    currentSymbol.background = drawable
                } else {
                    Toast.makeText(this@MainActivity, "Failed to retrieve weather", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
