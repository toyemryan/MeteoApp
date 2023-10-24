package com.example.meteoapp

import android.app.Application
import kotlinx.coroutines.selects.SelectInstance

class MyApplication: Application() {
    //compagion object per avere un riferimento all'istanza dell'applicazione
    companion object{
        lateinit var instance: MyApplication
    }

    //Metodo chiamato quando l'applicatione viene creata
    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    override fun onTerminate() {
        super.onTerminate()

        val sharedPreference = SharedPreference.getInstance(this)

        sharedPreference.clearCityValue()
    }
}