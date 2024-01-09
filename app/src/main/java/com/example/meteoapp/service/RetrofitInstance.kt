/*
 * Copyright (c) 09/01/2024 Toyem Tezem Ryan Parfait & Djouaka Kelefack Lionel all rights reserved
 */

package com.example.meteoapp.service

import com.example.meteoapp.Utility
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

