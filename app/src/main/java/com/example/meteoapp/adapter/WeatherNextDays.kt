package com.example.meteoapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meteoapp.R
import com.example.meteoapp.modal.WeatherList
import java.util.Locale

class WeatherNextDays : RecyclerView.Adapter<NextDaysHolder>() {

    private var listOfNextDaysWeather: List<WeatherList> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextDaysHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.list_today, parent, false)
        return NextDaysHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfNextDaysWeather.size
    }

    override fun onBindViewHolder(holder: NextDaysHolder, position: Int) {
        val nextDaysForecast = listOfNextDaysWeather[position]

        val imageResourceId = nextDaysForecast.weather[0].description?.let { getWeatherImageResourceId(it) }
        if (imageResourceId != null) {
            holder.weatherImageView.setImageResource(imageResourceId)
        }
        holder.timeDisplay.text = nextDaysForecast.dtTxt!!.substring(11, 16).toRegex().toString()

        val temperatureFahrenheit = nextDaysForecast.main?.temp
        val temperatureCelsius = ((temperatureFahrenheit?.minus(273.15))?.toInt())
        //val temperatureFormatted = String.format("%.0f", temperatureCelsius)
        holder.tempDisplay.text = "$temperatureCelsius °C"
    }

    private fun getWeatherImageResourceId(condition: String): Int {
        return when (condition.lowercase(Locale.getDefault())) {
            "clear sky" -> R.drawable.clear_sky
            "few clouds" -> R.drawable.few_clouds
            "scattered clouds" -> R.drawable.scattered_clouds
            "broken clouds" -> R.drawable.cloud
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
            else -> {
                Log.d("WeatherNexHourAdapter", "Using default image for condition: $condition")
                R.drawable.unknown
            }// Image par défaut si la condition n'est pas reconnue ou si l'image n'est pas trouvé
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setForecastList(weatherList: List<WeatherList>?){
        listOfNextDaysWeather = weatherList ?: emptyList()

        notifyDataSetChanged()
        Log.d("WeatherNexHourAdapter", "New data set: $weatherList")
    }
}
class NextDaysHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    val tempDisplay: TextView = itemView.findViewById(R.id.temperaturetoday)
    val timeDisplay: TextView = itemView.findViewById(R.id.humiditynexday)
    val weatherImageView: ImageView = itemView.findViewById(R.id.ImageMain)
}
