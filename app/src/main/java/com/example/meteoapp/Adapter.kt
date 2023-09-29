package com.example.meteoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter

class Adapter(private val context: Context, private val forecastList: List<DailyForecast>) :
    PagerAdapter() {

    override fun getCount(): Int {
        return forecastList.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_daily_forecast, container, false)
        val forecast = forecastList[position]

        val dateTextView = view.findViewById<TextView>(R.id.textDate)
        val temperatureTextView = view.findViewById<TextView>(R.id.textTemperature)
        val descriptionTextView = view.findViewById<TextView>(R.id.textDescription)
        val weatherIconImageView = view.findViewById<ImageView>(R.id.imageWeatherIcon)

        dateTextView.text = forecast.date
        temperatureTextView.text = "${forecast.temperature}°C"
        descriptionTextView.text = forecast.description
        weatherIconImageView.setImageResource(forecast.weatherIconResId)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}
