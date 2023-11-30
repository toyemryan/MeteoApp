package com.example.meteoapp.setting

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.meteoapp.R
import com.example.meteoapp.adapter.FinalListNextDay
import com.example.meteoapp.adapter.WeatherNextDays
import com.example.meteoapp.adapter.WeatherToday
import com.example.meteoapp.databinding.ActivitySettingBinding
import com.example.meteoapp.mainMeteo.MainMeteoViewModel
import com.example.meteoapp.setting.languageChange.DefaultLocaleHelper


private lateinit var binding: ActivitySettingBinding

private lateinit var mainMeteoViewModel: MainMeteoViewModel
class SettingActivity : AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var weatherNextDays:WeatherNextDays
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentsetting, SettingFragment())
            .commit()

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)

        //weatherTodayAdapter = WeatherToday()
        weatherNextDays = WeatherNextDays()

        mainMeteoViewModel = ViewModelProvider(this).get(MainMeteoViewModel::class.java)
        observeTemperatureLiveData()

    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        //Log.d("onSharedPreferenceChanged", "il listener che funziona come un callback")
        if(key == "lingua"){
            val prefs = sharedPreferences?.getString(key, "1")
            val lang = DefaultLocaleHelper.getInstance(this)
            when(prefs?.toInt()){
                1 ->{
                    lang.setCurrentLocale("it")
                    recreate()
                    }
                2 ->{
                    lang.setCurrentLocale("fr")
                    recreate()
                }
            }
        }
    }

    private fun observeTemperatureLiveData() {
        mainMeteoViewModel.maintempature.observe(this) { temperature ->
            val unit = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("Temperature", "1")
            temperature?.let { updateTemperatureUnit(unit) }
        }

    }

    private fun updateTemperatureUnit(unit: String?) {
        val listOfNextDaysWeather = weatherNextDays.getListOfNextDays()
        val convertedTemperatureList = mutableListOf<FinalListNextDay>()

        weatherNextDays.updateTemperatureUnit(unit?.toInt() ?: 1)
        listOfNextDaysWeather.forEach { weatherData ->
            val temperatureKelvin = weatherData.tempMax ?: 0.0
            val temperatureCelsius = when (unit?.toIntOrNull()) {
                1 -> temperatureKelvin - 273.15 // Celsius
                2 -> (temperatureKelvin - 273.15) * 9 / 5 + 32 // Fahrenheit
                else -> temperatureKelvin - 273.15 // Par défaut Celsius
            }

            // Mise à jour la température dans l'objet FinalListNextDay
            val updatedWeatherData = weatherData.copy(tempMax = temperatureCelsius)
            convertedTemperatureList.add(updatedWeatherData)
        }

        // Mise à jour les températures converties dans l'adaptateur
        weatherNextDays.setForecastList(convertedTemperatureList)
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

}


