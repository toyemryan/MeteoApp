package com.example.meteoapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meteoapp.R
import com.example.meteoapp.modal.WeatherList
import com.example.meteoapp.repository.Repository
import com.example.meteoapp.repository.calculMaxMinTemperature
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

    @SuppressLint("SimpleDateFormat", "SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: NextDaysHolder, position: Int) {
        val nextDaysForecastObject = listOfNextDaysWeather[position]


        val imageResourceId =
            Repository().getWeatherImageResourceId(nextDaysForecastObject.weather[0].description.orEmpty())
        holder.weatherImageView.setImageResource(imageResourceId)

        val dateInputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateInputFormat.parse(nextDaysForecastObject.dtTxt!!)
        val dateOuputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val dayName = dateOuputFormat.format(date!!)
        holder.day.text = dayName

        val dayMonthFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
        holder.dayMonth.text = dayMonthFormat.format(date)

        /*
        val minTemperature = nextDaysForecastObject.main?.tempMin
        val maxTemperature = nextDaysForecastObject.main?.tempMax

        val minTemperatureCelsius = ((minTemperature?.minus(273.15))?.toInt())
        val maxTemperatureCelsius = ((maxTemperature?.minus(273.15))?.toInt())

        holder.minTemperature.text = " $minTemperatureCelsius °C"
        holder.maxTemperature.text = " $maxTemperatureCelsius °C"

         */
         listOfNextDaysWeather.map { it.main!!.temp }
        val maxMinTemperature = calculMaxMinTemperature(listOfNextDaysWeather)

        if (maxMinTemperature != null) {
            val maxTemperature = maxMinTemperature.first
            val minTemperature = maxMinTemperature.second

            val minTemperatureCelsius = (minTemperature?.minus(273.15))?.toInt()
            val maxTemperatureCelsius = (maxTemperature?.minus(273.15))?.toInt()

            holder.minTemperature.text = " $minTemperatureCelsius °C"
            holder.maxTemperature.text = " $maxTemperatureCelsius °C"
        }
    }


        @SuppressLint("NotifyDataSetChanged")
    fun setForecastList(weatherList: List<WeatherList>?) {
        listOfNextDaysWeather = weatherList ?: emptyList()
        notifyDataSetChanged()
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

