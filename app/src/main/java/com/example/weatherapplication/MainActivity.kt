package com.example.weatherapplication

import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
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
    private lateinit var searchBar: SearchView
    private var apiKey = "" //My Api Key - Update to users key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialiseViews()

        // Fetch API key stored in local properties
        apiKey = getString(R.string.myApiKey)

        // Run method to fetch and update ui to reflect current weather in default city (Wellington)
        getWeatherData("Wellington")

        getWeatherForcast("Wellington")

        // Set up search bar and listener
        setupSearchBar()
    }

    private fun initialiseViews() {
        cityTextView = findViewById(R.id.currentCity)
        tempTextView = findViewById(R.id.currentTemp)
        descriptionTextView = findViewById(R.id.currentDescription)
        minTextView = findViewById(R.id.currentMin)
        maxTextView = findViewById(R.id.currentMax)
        humidityTextView = findViewById(R.id.currentHumidity)
        windTextView = findViewById(R.id.currentWind)
        currentSymbol = findViewById(R.id.currentSymbol)
    }

    private fun getWeatherData(cityName: String) {
        // Retrofit instance for converting
        val retrofit = ApiClient.retrofit
        val service = retrofit.create(WeatherApiService::class.java)
        // Make API call to OpenWeather
        val call = service.getWeather(cityName, apiKey, "metric")
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    cityTextView.text = "${weatherResponse?.name}"
                    tempTextView.text = buildString {
                        append(weatherResponse?.main?.temp)
                        append("°C ")
                    }
                    descriptionTextView.text = "${weatherResponse?.weather?.get(0)?.description}"
                    minTextView.text = buildString {
                        append(weatherResponse?.main?.temp_min)
                        append("°C")
                    }
                    maxTextView.text = buildString {
                        append(weatherResponse?.main?.temp_max)
                        append("°C")
                    }
                    humidityTextView.text = buildString {
                        append(weatherResponse?.main?.humidity)
                        append("%")
                    }
                    windTextView.text = buildString {
                        append(weatherResponse?.wind?.speed)
                        append(" m/s")
                    }
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
                    Toast.makeText(
                        this@MainActivity,
                        "Failed to retrieve weather",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getWeatherForcast(cityName: String) {

    }

    private fun setupSearchBar() {
        searchBar = findViewById(R.id.searchField)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    getWeatherData(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

}
