package com.example.meteoapp.mainMeteo


import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.meteoapp.R
import com.example.meteoapp.service.RetrofitInstance
import com.example.meteoapp.modal.WeatherList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


// Annotazione per richiedere API di almeno la versione specificata di Android
class MainMeteoViewModel (application: Application) : AndroidViewModel(application){


    //cinque giorni
    private val fiveDayWeatherLiveData = MutableLiveData<List<WeatherList>>()

    private val ancona = "Ancona"


    //LiveData per il nome della città
    private val _cityName = MutableLiveData("Ancona")
    val cityName: LiveData<String>
        get() = _cityName


    private val _maintemperature = MutableLiveData("25°C")
    val maintempature: LiveData<String>
        get() = _maintemperature

    // private val _hour = MutableLiveData<String>()
    //val hour: LiveData<String>
    //  get() = _hour

    private  val _minTemp = MutableLiveData<String>()
    val minTemp: LiveData<String>
        get() = _minTemp

    private  val _maxTemp = MutableLiveData<String>()
    val maxTemp: LiveData<String>
        get() = _maxTemp

    private val _day = MutableLiveData<String?>()
    val day: MutableLiveData<String?>
        get() = _day

    private val _hour = MutableLiveData<String?>()
    val hour: MutableLiveData<String?>
        get() = _hour

    private val _seaLevel = MutableLiveData<String>()
    val seaLevel: LiveData<String>
        get() = _seaLevel

    private val _windSpeed = MutableLiveData<String>()
    val windSpeed: LiveData<String>
        get() = _windSpeed

    private val _weatherImage = MutableLiveData<Int>()
    val weatherImage: LiveData<Int>
        get() = _weatherImage

    private val _feelLike = MutableLiveData<String>()
    val feelLike: MutableLiveData<String>
        get() = _feelLike

    private val _humidity = MutableLiveData<String>()
    val humidity: MutableLiveData<String>
        get() = _humidity

    private val _pressure = MutableLiveData<String>()
    val pressure: MutableLiveData<String>
        get() = _pressure

    private val _weatherCondition = MutableLiveData<String>()
    val weatherCondition: LiveData<String>
        get() = _weatherCondition

    private val _weatherImageResourceId = MutableLiveData<String>()
    val weatherImageResourceId: LiveData<String>
        get() = _weatherImageResourceId


    private val _sunriseTime = MutableLiveData<String>()
    val sunriseTime: LiveData<String>
        get() = _sunriseTime


    private val _sunsetTime = MutableLiveData<String>()
    val sunsetTime: LiveData<String>
        get() = _sunsetTime



    private fun convertTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getWeather() {
        GlobalScope.launch(Dispatchers.IO) {
            val call = try {
                RetrofitInstance.api.getCurrentWeatherByCity(ancona)
            } catch (e: IOException) {
                Log.e("FLux error", "Error: ${e.message}")
                return@launch
            } catch (e: HttpException) {
                Log.e("Connection error", "Error: ${e.message}")
                return@launch
            }

            val response = call.execute()
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {

                    val data = response.body()!!

                    _cityName.value= data.city!!.name.toString()// name

                    val firstWeather = data.weatherList[0]

                    // Extraire les informations nécessaires

                    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val localDateTime = LocalDateTime.parse(data.weatherList[0].dtTxt, dateTimeFormatter)
                    _day.value = localDateTime.format(DateTimeFormatter.ofPattern("EEEE"))// Jour
                    _hour.value = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm")) // Heure (à ajuster selon votre logique)
                    _feelLike.value = "Feel like : ${(firstWeather.main?.feelsLike?.minus(273.15))?.toInt()} °C"
                    _pressure.value = "${firstWeather.main?.pressure?.times(0.001)} Bar" // Pression
                    val seaLevelValue = firstWeather.main?.seaLevel
                    _seaLevel.value = "$seaLevelValue m"

                   // val sys = firstWeather.sys
                    _sunriseTime.value = data.city!!.sunrise?.let { convertTimestampToTime(it.toLong()) }
                    _sunsetTime.value =  data.city!!.sunset?.let { convertTimestampToTime(it.toLong()) }


                    val minTempKelvin = data.weatherList[0].main?.tempMin
                    val maxTempKelvin = data.weatherList[0].main?.tempMax

                    val minTempCelsius = (minTempKelvin?.minus(273.15))
                    val maxTempCelsius = (maxTempKelvin?.minus(273.15))

                    if (minTempCelsius != null) {
                        _minTemp.value = "${minTempCelsius.toInt()}°C"
                    }

                    if (maxTempCelsius != null) {
                        _maxTemp.value = "${maxTempCelsius.toInt()}°C"
                    }

                    val windSpeedMeterSecond = firstWeather.wind?.speed
                    val windSpeedKmHour = windSpeedMeterSecond?.times(3.6)
                    _windSpeed.value = "${String.format("%.2f", windSpeedKmHour)} Km/h" // Vitesse du vent
                    _humidity.value = "${firstWeather.main?.humidity}%" // Humidité
                    //_weatherImageResourceId.value = getWeather(firstForecast.weather?.get(0)?.id) // ID de l'image
                    _pressure.value = "${firstWeather.main?.pressure?.times(0.001)} Bar" // Pression

                    _weatherCondition.value = firstWeather.weather?.get(0)?.description ?: "Erreur"


                    val temperatureKelvin = data.weatherList[0].main?.temp
                    val temperatureCelsius = (temperatureKelvin?.minus(273.15))
                    if (temperatureCelsius != null) {
                        _maintemperature.value = "${temperatureCelsius.toInt()}°C" // main temperature
                    }
                }
            }
        }
    }

}

