package com.example.meteoapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.meteoapp.R
import com.example.meteoapp.modal.WeatherList
import java.io.Serializable
import java.util.Locale
import java.util.concurrent.locks.Condition

class WeatherNextHour: RecyclerView.Adapter<ForecastViewHolder>() {
    private var listOfNextHourWeather = listOf<WeatherList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_nethour, parent, false)
        return ForecastViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfNextHourWeather.size
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val nexhourForeCast = listOfNextHourWeather[position]

        val imageResourceId = getWeatherImageResourceId(nexhourForeCast.weather ?: "")
        holder.weatherImageView.setImageResource(imageResourceId)
        holder.timeDisplay.text = nexhourForeCast.dtTxt!!.substring(11, 16).toRegex().toString()

        val temperatureFahrenheit = nexhourForeCast.main?.temp
        val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
        val temperatureFormatted = String.format("%.2f", temperatureCelsius)
        holder.tempDisplay.text = "$temperatureFormatted °C"
    }

    fun getWeatherImageResourceId(condition: Serializable): Int{
        return when(condition.toString().lowercase(Locale.getDefault())){
            "clear sky" -> R.drawable.clear_sky
            "few clouds" -> R.drawable.few_clouds
            "scattered clouds" -> R.drawable.scattered_clouds
            "broken clouds" -> R.drawable.cloud
            "shower rain" -> R.drawable.shower_rain
            "rain" -> R.drawable.rain
            "thunderstorm" -> R.drawable.thunderstorm
            "snow" -> R.drawable.snow
            "mist" -> R.drawable.mist
            else -> R.drawable.unknown // Image par défaut si la condition n'est pas reconnue ou si l'image n'est pas trouvé
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setForecastList(weatherList: List<WeatherList>?){
            listOfNextHourWeather = weatherList ?: emptyList()

            notifyDataSetChanged()
            Log.d("WeatherNexHourAdapter", "New data set: $weatherList")
        }
}
class ForecastViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val tempDisplay: TextView = itemView.findViewById(R.id.temperaturetoday)
    val timeDisplay: TextView = itemView.findViewById(R.id.humiditynexday)
    val weatherImageView: ImageView = itemView.findViewById(R.id.ImageMain)
}
