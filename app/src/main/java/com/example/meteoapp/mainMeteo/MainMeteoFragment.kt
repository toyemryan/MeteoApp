package com.example.meteoapp.mainMeteo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meteoapp.R

class MainMeteoFragment : Fragment() {

    companion object {
        fun newInstance() = MainMeteoFragment()
    }

    private lateinit var viewModel: MainMeteoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_meteo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainMeteoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}