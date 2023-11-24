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
import com.example.meteoapp.adapter.FinalListNextDay
import com.example.meteoapp.modal.WeatherList
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.Locale

open class Repository {
    companion object {
        var cityname : String? = null
        var citynow : String? = null
        var finalNextDay = FinalListNextDay()
    }


    fun listtt (): FinalListNextDay {
        return finalNextDay
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
            "overcast clouds" -> R.drawable.overcast_clouds
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
            Log.d("preferenceChange", "gps activé")
            if (!activity.let { Repository().isNetworkAvailable(it) }){
                Toast.makeText(activity,"Please put on your connection", Toast.LENGTH_SHORT).show()
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
                        activity,
                        100
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    fun requestDeviceLocationSettings(activity: Activity) {

        val manager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Repository().hasGPSDevice(activity)) {
            Log.e("TAG","Gps already enabled")
            //Toast.makeText(requireContext(), "Gps already enabled", Toast.LENGTH_SHORT).show()
        }
        if(!Repository().hasGPSDevice(activity)){
            Toast.makeText(activity,"Gps not Supported",Toast.LENGTH_SHORT).show()
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && Repository().hasGPSDevice(activity)) {
            //Log.e("TAG","Gps already enabled");
            Toast.makeText(activity,"Gps not enabled",Toast.LENGTH_SHORT).show()
            enableLoc(activity)
        }else{
            Log.e("TAG","Gps already enabled")
            //Toast.makeText(requireContext(),"Gps already enabled",Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun getCityName(lat: Double, long: Double, activity: Activity?): String? {
        var cityName: String? = null
        try {
            val geoCoder = activity?.let { Geocoder(it, Locale.getDefault()) }
            val adress = geoCoder?.getFromLocation(lat, long, 3)
            cityName = adress?.get(0)?.locality
        }
        catch (e: IOException){
            Log.e("TAG","Problema di network")
        }
        catch (e: NullPointerException){
            Toast.makeText(activity, "Please Refresh Again", Toast.LENGTH_SHORT).show()
        }
        return cityName
    }

}

fun calculMaxMinTemperature(weathers: List<WeatherList>): Pair<Double?, Double?>? {
    if (weathers.isEmpty()) {
        return null
    }

    var maxTemperature = weathers[0].main?.temp
    var minTemperature = weathers[0].main?.temp

    for (weather in weathers) {
        val currentTemp = weather.main?.temp
        if (currentTemp != null) {
            if (currentTemp > (maxTemperature ?: Double.MIN_VALUE)) {
                maxTemperature = currentTemp
            }
            if (currentTemp < (minTemperature ?: Double.MAX_VALUE)) {
                minTemperature = currentTemp
            }
        }
    }

    return Pair(maxTemperature, minTemperature)
}

