package com.example.meteoapp.service

import com.example.meteoapp.Utility
import com.example.meteoapp.modal.ForeCast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("forecast?")
    fun getCurrentWeather(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("appid") appid:String = Utility.API_KEY // appid:String
    ):Call<ForeCast>

    @GET("forecast?")
    fun getCurrentWeatherByCity(
        @Query("q") city:String,
        @Query("appid") appid: String = Utility.API_KEY
    ):Call<ForeCast>

    @GET("forecast?")
    fun getFutureWeatherByCity(
        @Query("q") city: String,
        @Query("cnt") days: Int = 5,
        @Query("appid") appid: String = Utility.API_KEY
    ): Call<ForeCast>

}