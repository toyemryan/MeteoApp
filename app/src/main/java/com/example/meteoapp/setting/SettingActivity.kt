package com.example.meteoapp.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.meteoapp.R
import com.example.meteoapp.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)

      // setSupportActionBar(binding.toolbar)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentsetting, SettingFragment())
            .commit()
    }
}
