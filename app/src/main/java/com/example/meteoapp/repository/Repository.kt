/*
 * Copyright (c) 09/01/2024 Toyem Tezem Ryan Parfait & Djouaka Kelefack Lionel all rights reserved
 */

package com.example.meteoapp.repository

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import com.example.meteoapp.R
import com.example.meteoapp.SharedPreferences
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.Locale


/**
 * al fine de evitare che il viewmodel MainMeteoViewModel sia un god Object, abbiamo creato questa
 * Classe che contiene certi metodi importanti
 */
open class Repository {
    companion object {
        var cityname : String? = null
        var citynow : String? = null
        var preftemp : String? = "1"
    }



    fun getWeatherImageResourceId(condition: String): Int {
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
            "cloud" -> R.drawable.cloud
            "overcast clouds" -> R.drawable.overcast_clouds
            "moisture" -> R.drawable.moisture
            "night overcast" -> R.drawable.night_overcast
            "overcast" -> R.drawable.overcast
            "overcast fog" ->R.drawable.overcast_fog
            "overcast mist" -> R.drawable.overcast_mist
            "partly cloudy" -> R.drawable.partly_cloudy
            "rain cloud" -> R.drawable.rain_cloud
            "rainbow" -> R.drawable.rainbow
            "raindrop" -> R.drawable.raindrop
            "rainfall" -> R.drawable.rainfall
            "rainy night" -> R.drawable.rainy_night
            "sky" -> R.drawable.sky
            "sleet" -> R.drawable.sleet
            "snowy" -> R.drawable.snowy
            "storm" -> R.drawable.storm
            "summer" -> R.drawable.summer
            "sun" -> R.drawable.sun
            "sunny" -> R.drawable.sunny
            "Storm With Heavy Rain" ->  R.drawable.storm_with_heavy_rain
            "Heavy Rain" ->  R.drawable.heavy_rain
            "Sun Behind Rain Cloud" ->  R.drawable.sun_behind_rain_cloud
            "Moderate Rain" ->  R.drawable.moderate_rain
            "Torrential Rain" ->  R.drawable.torrential_rain
            "heavy intensity rain" -> R.drawable.heavy_intensity_rain
            "moderate rain" -> R.drawable.moderate_rain
            "mist" -> R.drawable.mist
            else -> {
                Log.d("WeatherNextDays", "Using default image for condition: $condition")
                R.drawable.unknown
            }
        }
    }

    private fun hasGPSDevice(context: Context): Boolean {
        val mgr = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = mgr.allProviders
        return providers.contains(LocationManager.GPS_PROVIDER)
    }

    /**
     * Funzione che verifica se c'è internet, la particularità qua è che verifica se c'è la rete internet
     * disponibile via WIFI, TELEPHONO, ETHERNET o BLUETOOTH
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    /**
     * Questa funzione propone all'utente di attivare la geolocalizzazione, l'utente può accettare
     * o rifiutare
     */
    private fun enableLoc(activity : Activity){

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(100)
            .build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = activity.let { LocationServices.getSettingsClient(it) }
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            //Log.d("preferenceChange", "gps activé")
            if (!activity.let { Repository().isNetworkAvailable(it) }){
                Toast.makeText(activity,R.string.active_connection, Toast.LENGTH_SHORT).show()
            }

        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        activity, 100
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    /**
     * Questa funzione si occupa di informare se la geolocalizzazione è attivata con la chiamata al
     * metodo hasGPSDevice(),se non chiamiamo
     * il metodo enableloc()
     */
    fun requestDeviceLocationSettings(activity: Activity) {

        val manager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Repository().hasGPSDevice(activity)) {
            //Log.e("TAG","Gps already enabled")
            //Toast.makeText(requireContext(), "Gps already enabled", Toast.LENGTH_SHORT).show()
        }
        if(!Repository().hasGPSDevice(activity)){
            Toast.makeText(activity,R.string.error_gps,Toast.LENGTH_SHORT).show()
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Repository().hasGPSDevice(activity)) {
            //Log.e("TAG","Gps already enabled");
            Toast.makeText(activity,R.string.gps_non_abilitato,Toast.LENGTH_SHORT).show()
            enableLoc(activity)
        }else{
            //Log.e("TAG","Gps already enabled")
            Toast.makeText(activity,R.string.gps_enabled,Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Questa funzione fa la conversione delle coordinate Longitude e latitude trovando la città
     */
    @SuppressLint("SuspiciousIndentation")
    fun getCityName(lat: Double, long: Double, activity: Activity?): String? {
        var cityName: String? = null
        try {
            val geoCoder = activity?.let { Geocoder(it, Locale.getDefault()) }
            val adress = geoCoder?.getFromLocation(lat, long, 3)
            cityName = adress?.get(0)?.locality
        }
        catch (e: IOException){
            //Log.e("TAG","Problema di network")
            Toast.makeText(activity, R.string.error_network, Toast.LENGTH_SHORT).show()
        }
        catch (e: NullPointerException){
            Toast.makeText(activity, R.string.refresh, Toast.LENGTH_SHORT).show()
        }
        return cityName
    }

}


