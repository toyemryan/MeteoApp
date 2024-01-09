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
import com.example.meteoapp.modal.WeatherList
import com.example.meteoapp.repository.Repository

/**
 * l'adapter del recycler view che contiene i dati meteo in intervallo di 3 ore
 */
class WeatherNextHour :  RecyclerView.Adapter<NextHourHolder>() {
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

        val imageResourceId = nexhourForeCast.weather[0].description?.let { Repository().getWeatherImageResourceId(it) }
        if (imageResourceId != null) {
            holder.weatherImageView.setImageResource(imageResourceId)
        }
        holder.timeDisplay.text = nexhourForeCast.dtTxt?.substring(11, 16).toString()

        val temperatureKelvin = nexhourForeCast.main?.temp
        val temperatureCelsius = ((temperatureKelvin?.minus(273.15))?.toInt())

        val temperatureFahrenheit = (((temperatureKelvin?.minus(273.15))?.times(1.8))?.plus(32))
            if (Repository.preftemp == "1") {
                holder.tempDisplay.text = "$temperatureCelsius °C"
            } else {
                holder.tempDisplay.text = "${temperatureFahrenheit?.toInt()} °F"
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setForecastList(weatherList: List<WeatherList>?){
            listOfNextHourWeather = weatherList ?: emptyList()
            notifyDataSetChanged()
        }
    }
class NextHourHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val tempDisplay: TextView = itemView.findViewById(R.id.temperaturetoday)
    val timeDisplay: TextView = itemView.findViewById(R.id.humiditynexday)
    val weatherImageView: ImageView = itemView.findViewById(R.id.ImageMain)
}
