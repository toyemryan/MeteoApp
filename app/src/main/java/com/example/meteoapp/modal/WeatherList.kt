/*
 * Copyright (c) 09/01/2024 Toyem Tezem Ryan Parfait & Djouaka Kelefack Lionel all rights reserved
 */

package com.example.meteoapp.modal

import com.google.gson.annotations.SerializedName
import com.lionel.mameteo.modal.Clouds
import com.lionel.mameteo.modal.Main
import com.lionel.mameteo.modal.Sys
import com.lionel.mameteo.modal.Weather
import com.lionel.mameteo.modal.Wind


data class WeatherList (

  @SerializedName("dt"         ) var dt         : Int?               = null,
  @SerializedName("main"       ) var main       : Main?              = Main(),
  @SerializedName("weather"    ) var weather    : ArrayList<Weather> = arrayListOf(),
  @SerializedName("clouds"     ) var clouds     : Clouds?            = Clouds(),
  @SerializedName("wind"       ) var wind       : Wind?              = Wind(),
  @SerializedName("visibility" ) var visibility : Int?               = null,
  @SerializedName("pop"        ) var pop        : Double?            = null,
  @SerializedName("sys"        ) var sys        : Sys?               = Sys(),
  @SerializedName("dt_txt"     ) var dtTxt      : String?            = null

)