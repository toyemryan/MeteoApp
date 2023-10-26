package com.example.meteoapp.service

import com.example.meteoapp.Utility
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: Service by lazy {
            Retrofit.Builder()
                .baseUrl(Utility.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Service::class.java)

        }

}

