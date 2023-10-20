package com.example.meteoapp.service

import com.example.meteoapp.Utility
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        Retrofit.Builder().baseUrl(Utility.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build()
    }
    val api by lazy {
        retrofit.create(Service::class.java)
    }
}
