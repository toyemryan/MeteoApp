package com.example.meteoapp.repository

import android.util.Log
import com.example.meteoapp.R
import com.example.meteoapp.modal.WeatherList
import java.util.Locale

object ResourceImage {
    fun getWeatherImageResourceId(condition: String): Int {
        return when (condition.lowercase(Locale.getDefault())) {
            "clear sky" -> R.drawable.clear_sky
            "few clouds" -> R.drawable.few_clouds
            "scattered clouds" -> R.drawable.scattered_clouds
            "broken clouds" -> R.drawable.broken_clouds
            "shower rain" -> R.drawable.shower_rain
            "rain" -> R.drawable.rain
            "thunderstorm" -> R.drawable.thunderstorm
            "snow" -> R.drawable.snow
            "mist" -> R.drawable.mist
            "light rain" -> R.drawable.light_rain
            "fog" -> R.drawable.fog
            "haze" -> R.drawable.haze
            "smoke" -> R.drawable.smoke
            "very cold" -> R.drawable.very_cold
            "warm" -> R.drawable.warm
            "winds" -> R.drawable.wind
            "feels like" -> R.drawable.feels_like
            "overcast clouds" -> R.drawable.overcast_clouds
            else -> {
                Log.d("WeatherNextDays", "Using default image for condition: $condition")
                R.drawable.unknown
            }
        }
    }
}

fun calculMaxMinTemperature(weathers: List<WeatherList>): Pair<Double?, Double?>? {
    if (weathers.isEmpty()) {
        return null
    }

    var maxTemperature = weathers[0].main?.temp
    var minTemperature = weathers[0].main?.temp

    for (weather in weathers) {
        val currentTemp = weather.main?.temp
        if (currentTemp != null) {
            if (currentTemp > (maxTemperature ?: Double.MIN_VALUE)) {
                maxTemperature = currentTemp
            }
            if (currentTemp < (minTemperature ?: Double.MAX_VALUE)) {
                minTemperature = currentTemp
            }
        }
    }

    return Pair(maxTemperature, minTemperature)
}

