package com.example.meteoapp.mainMeteo

import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meteoapp.MyApplication
import com.example.meteoapp.service.RetrofitInstance
import com.lionel.mameteo.modal.WeatherList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*
import java.time.format.DateTimeFormatter


class MainMeteoViewModel : ViewModel() {
    //LiveData per i dati meteorologici odierni e futuri
    val todayWeatherLiveData = MutableLiveData<List<WeatherList>>()
    //cinque giorni

    //LiveData per i dati meteorologici piu vicini con la stessa data
    val closetorexactlysameweatherdata = MutableLiveData<WeatherList?>()

    //LiveData per il nome della città
    val cityName = MutableLiveData<String?>()

    //contesto dell'applicazione
    val context = MyApplication.instance

    // Funzione per ottenere i dati meteorologici (odierni o futuri) in base alla città o alle coordinate

    fun getWeather(city:String? = null, lat:String? = null, lon:String? = null) = viewModelScope.launch(Dispatchers.IO){
        val weatherTodayList = mutableListOf<WeatherList>()
        val currentDateTime = LocalDateTime.now()
        val currentDate0 = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        Log.e("ViewModelCoordinates", "$lat $lon")

        //chiamata API in base alla citta o alle coordinate
        val call = if (city != null){
            RetrofitInstance.api.getCurrentWeatherByCity(city)
        } else {
            RetrofitInstance.api.getCurrentWeather(lat!!, lon!!)
        }

        //esecuzione della chiamata API per otttenere la risposta
        var response = call.execute()

        if (response.isSuccessful){
            val weatherList = response.body()?.weatherList

            cityName.postValue(response.body()?.city!!.name)

            val currentDate = currentDate0

            // Filtrare i dati meteorologici odierni
            weatherList?.forEach{
                weather ->
                if (weather.dtTxt!!.split("\\s".toRegex()).contains(currentDate)){
                    weatherTodayList.add(weather)
                }
            }

            // Trovare il dato meteorologico più vicino con la stessa data
            val closestWeather = findClosestWeather(weatherTodayList)
            closetorexactlysameweatherdata.postValue(closestWeather)

            // Aggiorna la LiveData con i dati meteorologici odierni
            todayWeatherLiveData.postValue(weatherTodayList)
        }else{
            // Gestione dell'errore in caso di chiamata non riuscita
            val errorMessage = response.message()
            Log.e("CurrentWeatherError", "Error: $errorMessage")
        }
    }


    // Funzione per trovare il dato meteorologico più vicino in base all'ora corrente
    private fun findClosestWeather(weatherList: List<WeatherList>): WeatherList? {
        val systemTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        var closestWeather: WeatherList? = null
        var minTimeDifference = Int.MIN_VALUE

        //iterare  attraverso i dati meteorologici e trovare il piu vicino
        for (weather in weatherList){
            val weatherTime = weather.dtTxt!!.substring(11, 16)
            val timeDifference = Math.abs(timeToMinutes(weatherTime) - timeToMinutes(systemTime))

            if (timeDifference < minTimeDifference){
                minTimeDifference = timeDifference
                closestWeather = weather
            }
        }
        return closestWeather
    }

    // Funzione per convertire l'ora in minuti
    private fun timeToMinutes(time: String): Int {
        // Dividi la stringa del tempo utilizzando il carattere ":" come delimitatore
        val parts = time.split(":")

        // Converti la prima parte (ore) in un intero, moltiplica per 60 e aggiungi la seconda parte (minuti)
        return parts[0].toInt() * 60 + parts[1].toInt()
    }
}

