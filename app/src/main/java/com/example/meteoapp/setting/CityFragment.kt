package com.example.meteoapp.setting

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.meteoapp.R

class CityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city, container, false)
    }

    @SuppressLint("ResourceType")
    override fun onResume() {
        super.onResume()
        val toolbarTitle = (activity as AppCompatActivity).findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = getString(R.id.cityFragment)
    }
}