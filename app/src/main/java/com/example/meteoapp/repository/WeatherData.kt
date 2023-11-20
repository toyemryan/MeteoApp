package com.example.meteoapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.meteoapp.modal.WeatherList

class WeatherData {
    val weatherNexHour = MutableLiveData<List<WeatherList>>()
    val weatherNextDays = MutableLiveData<List<WeatherList>?>()
    val cityName = MutableLiveData<String>("Ancona")
    val maintemperature = MutableLiveData<String>("25°C")
    val minTemp = MutableLiveData<String>()
    val maxTemp = MutableLiveData<String>()
    val day = MutableLiveData<String?>()
    val hour = MutableLiveData<String?>()
    val windSpeed = MutableLiveData<String>()
    val feelLike = MutableLiveData<String>()
    val humidity = MutableLiveData<String>()
    val pressure = MutableLiveData<String>()
    val weatherCondition = MutableLiveData<String>()
    val weatherImageResourceId = MutableLiveData<Int>()
    val sunriseTime = MutableLiveData<String>()
    val sunsetTime = MutableLiveData<String>()
    val rain = MutableLiveData<String>()
    val visibility = MutableLiveData<String>()
    val cloudiness = MutableLiveData<String>()
}