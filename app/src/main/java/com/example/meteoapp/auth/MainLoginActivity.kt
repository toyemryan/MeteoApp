/*
 * Copyright (c) 09/01/2024 Toyem Tezem Ryan Parfait & Djouaka Kelefack Lionel all rights reserved
 */

package com.example.meteoapp.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.meteoapp.Coordinator
import com.example.meteoapp.MainActivity
import com.example.meteoapp.R
import com.example.meteoapp.databinding.ActivityMainLoginBinding
import com.example.meteoapp.setting.languageChange.DefaultLocaleHelper

class MainLoginActivity : AppCompatActivity(), Coordinator {

    private lateinit var binding : ActivityMainLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_login)

    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(DefaultLocaleHelper.getInstance(newBase!!).onAttach())
    }

    override fun gotomainmeteo (){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}