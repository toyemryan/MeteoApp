package com.example.meteoapp.modal

import com.google.gson.annotations.SerializedName
import com.lionel.mameteo.modal.City
import com.lionel.mameteo.modal.WeatherList


data class ForeCast (

    @SerializedName("cod"     ) var cod     : String?         = null,
    @SerializedName("message" ) var message : Int?            = null,
    @SerializedName("cnt"     ) var cnt     : Int?            = null,
    @SerializedName("list"    ) var weatherList    : ArrayList<WeatherList> = arrayListOf(),
    @SerializedName("city"    ) var city    : City?           = City()

)