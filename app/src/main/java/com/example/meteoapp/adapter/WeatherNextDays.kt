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
import java.text.SimpleDateFormat
import java.util.Locale
class WeatherNextDays : RecyclerView.Adapter<NextDaysHolder>() {

    private var listOfNextDaysWeather: List<WeatherList> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextDaysHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_nexdays, parent, false)
        return NextDaysHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfNextDaysWeather.size
    }


    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: NextDaysHolder, position: Int) {
        val nextDaysForecastObject = listOfNextDaysWeather[position]


        val imageResourceId = getWeatherImageResourceId(nextDaysForecastObject.weather[0].description.orEmpty())
        holder.weatherImageView.setImageResource(imageResourceId)

        val dateInputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateInputFormat.parse(nextDaysForecastObject.dtTxt!!)
        val dateOuputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val dayName = dateOuputFormat.format(date!!)
        holder.day.text = dayName

        val dayMonthFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
        holder.dayMonth.text = dayMonthFormat.format(date)


        //val temperatureFahrenheit = nextDaysForecastObject.main?.temp
        //val temperatureCelsius = ((temperatureFahrenheit?.minus(273.15))?.toInt())
       // holder.temp.text = "$temperatureCelsius °C"

        val minTemperature = nextDaysForecastObject.main?.tempMin
        val maxTemperature = nextDaysForecastObject.main?.tempMax

        val minTemperatureCelsius = ((minTemperature?.minus(273.15))?.toInt())
        val maxTemperatureCelsius = ((maxTemperature?.minus(273.15))?.toInt())

        holder.minTemperature.text = " $minTemperatureCelsius °C"
        holder.maxTemperature.text = " $maxTemperatureCelsius °C"
    }

    private fun getWeatherImageResourceId(condition: String): Int {
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

    @SuppressLint("NotifyDataSetChanged")
    fun setForecastList(weatherList: List<WeatherList>?) {
        listOfNextDaysWeather = weatherList ?: emptyList()
        notifyDataSetChanged()
        Log.d("WeatherNextDays", "New data set: $weatherList")
    }
}

class NextDaysHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val day: TextView = itemView.findViewById(R.id.day)
    val dayMonth: TextView = itemView.findViewById(R.id.dateDisplay)
    val weatherImageView: ImageView = itemView.findViewById(R.id.ImageMain)
    //val temp: TextView = itemView.findViewById(R.id.temperature)
    val minTemperature: TextView = itemView.findViewById(R.id.minTemperature)
    val maxTemperature: TextView = itemView.findViewById(R.id.maxTemperature)
}
