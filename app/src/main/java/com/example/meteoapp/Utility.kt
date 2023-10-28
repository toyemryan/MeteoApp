package com.example.meteoapp

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Utility {
    companion object{
        var BASE_URL : String = "https://api.openweathermap.org/data/2.5/"
        var API_KEY : String = "0332ad98e737e68c5fac5e96935461a0"

        const val PERMISSION_REQUEST_CODE = 123

        const val LOCATION_PERMISSION_REQUEST_CODE = 1001

        fun getFormattedDayAndDate(timestamp: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp * 1000

            val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
            val dateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)

            return "$dayOfWeek, $formattedDate"
        }
    }

}