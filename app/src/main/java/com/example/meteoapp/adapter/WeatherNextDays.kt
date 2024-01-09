/*
 * Copyright (c) 09/01/2024 Toyem Tezem Ryan Parfait & Djouaka Kelefack Lionel all rights reserved
 */

package com.example.meteoapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meteoapp.R
import com.example.meteoapp.repository.Repository
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * l'adapter del recycler view che contiene i dati dei 5 prossimi giorni
 */
class WeatherNextDays : RecyclerView.Adapter<NextDaysHolder>() {

    private var listOfNextDaysWeather: List<FinalListNextDay> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextDaysHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_nexdays, parent, false)
        return NextDaysHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfNextDaysWeather.size
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: NextDaysHolder, position: Int) {
        val nextDaysForecastObject = listOfNextDaysWeather[position]

        val imageResourceId =
            Repository().getWeatherImageResourceId(nextDaysForecastObject.description.orEmpty())
        holder.weatherImageView.setImageResource(imageResourceId)

        val dateInputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateInputFormat.parse(nextDaysForecastObject.dtTxt!!)
        val dateOuputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val dayName = dateOuputFormat.format(date!!)
        holder.day.text = dayName

        val dayMonthFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
        holder.dayMonth.text = dayMonthFormat.format(date)

        val minTemperatureCelsius = (nextDaysForecastObject.tempMin?.minus(273.15))?.toInt()
        val maxTemperatureCelsius = (nextDaysForecastObject.tempMax?.minus(273.15))?.toInt()


        val mintemperatureFahrenheit = ((minTemperatureCelsius)?.times(1.8))?.plus(32)
        val maxtemperatureFahrenheit = ((maxTemperatureCelsius)?.times(1.8))?.plus(32)

        if (Repository.preftemp == "1") {
            holder.minTemperature.text = " $minTemperatureCelsius °C"
            holder.maxTemperature.text = " $maxTemperatureCelsius °C"
        } else {
            holder.minTemperature.text = " $mintemperatureFahrenheit °F"
            holder.maxTemperature.text = " $maxtemperatureFahrenheit °F"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setForecastList(weatherList: List<FinalListNextDay>?) {
        listOfNextDaysWeather = (weatherList ?: emptyList())
        notifyDataSetChanged()
    }

}
class NextDaysHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val day: TextView = itemView.findViewById(R.id.day)
    val dayMonth: TextView = itemView.findViewById(R.id.dateDisplay)
    val weatherImageView: ImageView = itemView.findViewById(R.id.ImageMain)
    val minTemperature: TextView = itemView.findViewById(R.id.minTemperature)
    val maxTemperature: TextView = itemView.findViewById(R.id.maxTemperature)
}
