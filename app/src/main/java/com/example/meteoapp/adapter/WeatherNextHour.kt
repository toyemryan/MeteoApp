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
import com.example.meteoapp.repository.ResourceImage.getWeatherImageResourceId


class WeatherNextHour: RecyclerView.Adapter<NextHourHolder>() {
    private var listOfNextHourWeather = listOf<WeatherList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextHourHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_nexhour, parent, false)
        return NextHourHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfNextHourWeather.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NextHourHolder, position: Int) {
        val nexhourForeCast = listOfNextHourWeather[position]

        val imageResourceId = nexhourForeCast.weather[0].description?.let { getWeatherImageResourceId(it) }
        if (imageResourceId != null) {
            holder.weatherImageView.setImageResource(imageResourceId)
        }
        holder.timeDisplay.text = nexhourForeCast.dtTxt?.substring(11, 16).toString()


        val temperatureFahrenheit = nexhourForeCast.main?.temp
        val temperatureCelsius = ((temperatureFahrenheit?.minus(273.15))?.toInt())
        // val temperatureFormatted = String.format("%.2f", temperatureCelsius)
        holder.tempDisplay.text = "$temperatureCelsius °C"
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setForecastList(weatherList: List<WeatherList>?){
            listOfNextHourWeather = weatherList ?: emptyList()

            notifyDataSetChanged()
            Log.d("WeatherNexHourAdapter", "New data set: $weatherList")
        }
}
class NextHourHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val tempDisplay: TextView = itemView.findViewById(R.id.temperaturetoday)
    val timeDisplay: TextView = itemView.findViewById(R.id.humiditynexday)
    val weatherImageView: ImageView = itemView.findViewById(R.id.ImageMain)
}
