package com.example.meteoapp.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.example.meteoapp.R
import com.example.meteoapp.databinding.ActivitySettingBinding
import com.example.meteoapp.setting.languageChange.DefaultLocaleHelper

class SettingActivity : AppCompatActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {

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


    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.d("preferenceChange", "il listener che funziona come un callback")

        if(key == "lingua"){
            val prefs = sharedPreferences?.getString(key, "1")
            val lang = DefaultLocaleHelper.getInstance(this)
            when(prefs?.toInt()){
                1 ->{
                    Log.d("preferenceChange", "è stato selezionato il 1")
                    lang.setCurrentLocale("it")
                    recreate()
                    }
                2 ->{
                    Log.d("preferenceChange", "è stato selezionato il 2")
                    lang.setCurrentLocale("fr")
                    recreate()
                }
                3 ->{
                    Log.d("preferenceChange", "è stato selezionato il 3")
                    lang.setCurrentLocale("en")
                    recreate()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

}
