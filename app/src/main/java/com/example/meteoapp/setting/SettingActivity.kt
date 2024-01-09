/*
 * Copyright (c) 09/01/2024 Toyem Tezem Ryan Parfait & Djouaka Kelefack Lionel all rights reserved
 */

package com.example.meteoapp.setting

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.example.meteoapp.MainActivity
import com.example.meteoapp.R
import com.example.meteoapp.adapter.WeatherNextDays
import com.example.meteoapp.databinding.ActivitySettingBinding
import com.example.meteoapp.repository.Repository
import com.example.meteoapp.setting.languageChange.DefaultLocaleHelper

/**
 * La best practice Setting Preference API, si basa su tre elementi, preference.xml dove implementiamo
 * La UI, Setting Fragment dove si aggancia il file XML e SettingActivity che contiene la logica
 */
class SettingActivity : AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var weatherNextDays:WeatherNextDays
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentsetting, SettingFragment())
            .commit()

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)

        //weatherTodayAdapter = WeatherToday()
        weatherNextDays = WeatherNextDays()

    }

    /**
     * Funzione di callback che gestisce la scelta dell'utente
     */
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        if(key == "lingua"){
            val prefs = sharedPreferences?.getString(key, "1")
            val lang = DefaultLocaleHelper.getInstance(this)
            when(prefs?.toInt()){
                1 ->{
                    lang.setCurrentLocale("it")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    }
                2 ->{
                    lang.setCurrentLocale("fr")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        if(key == "Temperature"){

            val rf = sharedPreferences?.getString(key, "1")
            when(rf?.toInt()){
                1 ->{ // C°
                    Repository.preftemp = "1"
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                2 ->{ // F°
                    Repository.preftemp = "2"
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(DefaultLocaleHelper.getInstance(newBase!!).onAttach())
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

}


