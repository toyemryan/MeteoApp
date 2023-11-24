package com.example.meteoapp.mainMeteo

import LocationPermission
import android.app.Activity
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.meteoapp.adapter.FinalListNextDay
import com.example.meteoapp.modal.WeatherList
import com.example.meteoapp.repository.Repository
import com.example.meteoapp.service.RetrofitInstance.api
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class MainMeteoViewModel(application: Application) : AndroidViewModel(application) {

    private var _finalListNextDay = MutableLiveData<List<FinalListNextDay>?>()
    val finalListNextDay: MutableLiveData<List<FinalListNextDay>?>
        get() = _finalListNextDay

    private val _weathernexhour = MutableLiveData<List<WeatherList>>()
    val weatherNexHour: LiveData<List<WeatherList>>
        get() = _weathernexhour

    private val _weatherNextDays = MutableLiveData<List<WeatherList>?>()
    val weatherNextDays: MutableLiveData<List<WeatherList>?>
        get() = _weatherNextDays

    //private val city = "Ancona"

    // LiveData pour le nom de la ville
    private val _cityName = MutableLiveData<String>()
    val cityName: LiveData<String>
        get() = _cityName

    private val _maintemperature = MutableLiveData<String>()
    val maintempature: LiveData<String>
        get() = _maintemperature

    private val _minTemp = MutableLiveData<String>()
    val minTemp: LiveData<String>
        get() = _minTemp

    private val _maxTemp = MutableLiveData<String>()
    val maxTemp: LiveData<String>
        get() = _maxTemp

    private val _day = MutableLiveData<String?>()
    val day: MutableLiveData<String?>
        get() = _day

    private val _hour = MutableLiveData<String?>()
    val hour: MutableLiveData<String?>
        get() = _hour

    private val _windSpeed = MutableLiveData<String>()
    val windSpeed: LiveData<String>
        get() = _windSpeed

    private val _feelLike = MutableLiveData<String>()
    val feelLike: LiveData<String>
        get() = _feelLike

    private val _humidity = MutableLiveData<String>()
    val humidity: LiveData<String>
        get() = _humidity

    private val _pressure = MutableLiveData<String>()
    val pressure: LiveData<String>
        get() = _pressure

    private val _weatherCondition = MutableLiveData<String>()
    val weatherCondition: LiveData<String>
        get() = _weatherCondition

    private val _weatherImageResourceId = MutableLiveData<Int>()
    val weatherImageResourceId: LiveData<Int>
        get() = _weatherImageResourceId

    private val _sunriseTime = MutableLiveData<String>()
    val sunriseTime: LiveData<String>
        get() = _sunriseTime

    private val _sunsetTime = MutableLiveData<String>()
    val sunsetTime: LiveData<String>
        get() = _sunsetTime

    private val _rain = MutableLiveData<String>()
    val rain: LiveData<String>
        get() = _rain

    private val _visibility = MutableLiveData<String>()
    val visibility: LiveData<String>
        get() = _visibility

    private val _cloudiness = MutableLiveData<String>()
    val cloudiness: LiveData<String>
        get() = _cloudiness

    private lateinit var locationPermission: LocationPermission

    private fun convertTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(date)
    }
    fun setLocationPermission(permission: LocationPermission) {
        locationPermission = permission
    }
    private suspend fun <T> ApiCall(api: suspend () -> T) {
        return withContext(Dispatchers.IO){
            try {
                api.invoke()
            } catch (e: IOException) {
                Log.e("Flux error", "Error: ${e.message}")
            } catch (e: HttpException) {
                Log.e("Connection error", "Error: ${e.message}")
            }catch (e: SocketTimeoutException) {
                Log.e("Timeoutconnection", "Error: ${e.message}")
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getWeather() {
        GlobalScope.launch(Dispatchers.IO) {
            val call = try {
                Repository.cityname?.let { api.getCurrentWeatherByCity(it) } //api.getCurrentWeatherByCity(city)
            } catch (e: IOException) {
                Log.e("FLux error", "Error: ${e.message}")
                return@launch
            } catch (e: HttpException) {
                Log.e("Connection error", "Error: ${e.message}")
                return@launch
            }

            val response = call?.execute()
            if (response != null) {
                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {

                        val data = response.body()!!

                        _cityName.value= data.city!!.name.toString()// name

                        val firstWeather = data.weatherList[0]
                        Log.d("city value", "${Repository.cityname}")

                        // Extraire les informations nécessaires

                        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        val localDateTime = LocalDateTime.parse(data.weatherList[0].dtTxt, dateTimeFormatter)
                        _day.value = localDateTime.format(DateTimeFormatter.ofPattern("EEEE"))// Jour
                        _hour.value = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm")) // Heure (à ajuster selon votre logique)
                        _feelLike.value = "Feel like : ${(firstWeather.main?.feelsLike?.minus(273.15))?.toInt()} °C"
                        _pressure.value = "${firstWeather.main?.pressure?.times(0.001)} Bar" // Pression

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
                        // val windSpeedKmHour = windSpeedMeterSecond?.times(3.6)
                        _windSpeed.value = "${(windSpeedMeterSecond?.times(3.6))?.toInt()} Km/h" // Vitesse du vent
                        _humidity.value = "${firstWeather.main?.humidity}%" // Humidité
                        //_weatherImageResourceId.value = getWeather(firstForecast.weather?.get(0)?.id) // ID de l'image
                        _pressure.value = "${(firstWeather.main?.pressure?.times(0.001))?.toInt()} Bar" // Pression
                        _weatherCondition.value = firstWeather.weather[0].description ?: "Erreur"

                        val temperatureKelvin = data.weatherList[0].main?.temp
                        val temperatureCelsius = (temperatureKelvin?.minus(273.15))
                        if (temperatureCelsius != null) {
                            _maintemperature.value = "${temperatureCelsius.toInt()}°C" // main temperature
                        }

                        _rain.value = "${(firstWeather.pop?.times(100))?.toInt()}%"

                        _visibility.value = "${(firstWeather.visibility?.times(0.001))?.toInt()} Km"

                        _cloudiness.value = "${(firstWeather.clouds?.all)}%"

                        _weatherCondition.value = firstWeather.weather[0].description ?: "Erreur"
                        _weatherImageResourceId.value = Repository().getWeatherImageResourceId(_weatherCondition.value ?: "")
                    }
                }
            }
        }
    }

    suspend fun getWeatherNexHour() = viewModelScope.launch {
        ApiCall {
            val call =
                Repository.cityname?.let { api.getCurrentWeatherByCity(it) } //cityname?.let { api.getCurrentWeatherByCity(it) }  // val call = api.getCurrentWeatherByCity(city)
            val response = call?.execute()
            if (response != null) {
                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        val data = response.body()!!
                        _weathernexhour.value = data.weatherList.take(10)
                    }
                }
            }
        }
    }

    suspend fun getWeatherNextDays() = viewModelScope.launch {
        val listOfNextDay  = mutableListOf<FinalListNextDay>()

        ApiCall {
            val call = Repository.cityname?.let { api.getCurrentWeatherByCity(it) }
            val response = call?.execute()
            if (response != null) {
                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        val data = response.body()!!
                        val currentDate = LocalDate.now()
                        val futureDates = (1..5).map { currentDate.plusDays(it.toLong()) }

                        for(i in 0..4){
                          val futureDataa = data.weatherList.filter {
                              it.dtTxt?.split(" ")?.get(0).toString() in futureDates[i].toString() }

                            // si puo scorporare dentro repository()
                            if(futureDataa.isNotEmpty()){
                                listOfNextDay.add(
                                    FinalListNextDay(
                                        dtTxt = futureDataa[0].dtTxt,
                                        tempMin = futureDataa.minBy { it.main!!.temp!! }.main?.temp,
                                        tempMax = futureDataa.maxBy { it.main!!.temp!! }.main?.temp,
                                        description = futureDataa.map { it.weather[0].description }.groupBy { it }.maxBy{ it.value.size }.key
                                )
                                )
                            }else{
                                break
                            }
                        }
                       _finalListNextDay.postValue(listOfNextDay)
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    fun fresh(activity: Activity?){
        if (activity?.let { Repository().isNetworkAvailable(it) } == true && Repository.cityname != null){
            getWeather()
            viewModelScope.launch {
                getWeatherNextDays()
                getWeatherNexHour()
            }
        }
    }

}
