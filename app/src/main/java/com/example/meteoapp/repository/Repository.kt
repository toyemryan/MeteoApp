package com.example.meteoapp.repository

import android.util.Log
import com.example.meteoapp.R
import com.example.meteoapp.modal.WeatherList
import java.util.Locale

open class Repository {
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
            "cloud" -> R.drawable.cloud
            "overcast clouds" -> R.drawable.overcast_clouds
            "moisture" -> R.drawable.moisture
            "night overcast" -> R.drawable.night_overcast
            "overcast" -> R.drawable.overcast
            "overcast fog" ->R.drawable.overcast_fog
            "overcast mist" -> R.drawable.overcast_mist
            "partly cloudy" -> R.drawable.partly_cloudy
            "rain cloud" -> R.drawable.rain_cloud
            "rainbow" -> R.drawable.rainbow
            "raindrop" -> R.drawable.raindrop
            "rainfall" -> R.drawable.rainfall
            "rainy night" -> R.drawable.rainy_night
            "sky" -> R.drawable.sky
            "sleet" -> R.drawable.sleet
            "snowy" -> R.drawable.snowy
            "storm" -> R.drawable.storm
            "summer" -> R.drawable.summer
            "sun" -> R.drawable.sun
            "sunny" -> R.drawable.sunny
            "Storm With Heavy Rain" ->  R.drawable.storm_with_heavy_rain
            "Heavy Rain" ->  R.drawable.heavy_rain
            "Sun Behind Rain Cloud" ->  R.drawable.sun_behind_rain_cloud
            "Moderate Rain" ->  R.drawable.moderate_rain
            "Torrential Rain" ->  R.drawable.torrential_rain
            "heavy intensity rain" -> R.drawable.heavy_intensity_rain
            "moderate rain" -> R.drawable.moderate_rain
            "mist" -> R.drawable.mist
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

