package com.example.meteoapp.mainMeteo

import LocationPermission
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.meteoapp.R
import com.example.meteoapp.SharedPreferences
import com.example.meteoapp.adapter.WeatherNextDays
import com.example.meteoapp.adapter.WeatherNextHour
import com.example.meteoapp.databinding.FragmentMainMeteoBinding
import com.example.meteoapp.repository.Repository
import kotlinx.coroutines.launch
import java.util.*


class MainMeteoFragment : Fragment() {

    private var getCity : String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var locationPermission: LocationPermission
    private lateinit var binding: FragmentMainMeteoBinding
    private val viewModel: MainMeteoViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_meteo, container, false)
        sharedPreferences = SharedPreferences(requireContext())

        location()

        viewModel.fresh(requireActivity())
        swipeRefresh()
        return binding.root
    }


    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.S)
    private fun location(){
        if(activity != null && isAdded){
            locationPermission = LocationPermission(requireContext())
            viewModel.setLocationPermission(locationPermission)
            locationPermission.requestLocationPermission { granted ->
                if (granted) {
                    Repository().requestDeviceLocationSettings(requireActivity())
                    if (activity?.let { Repository().isNetworkAvailable(it) } == true){
                        locationPermission.requestLocationUpdates { location ->
                            location.let { it ->
                                if (activity?.let { Repository().isNetworkAvailable(it) } == true){
                                    getCity = Repository().getCityName(it.latitude, it.longitude, requireActivity())
                                    //Log.d("preferenceChange", " $getCity latitude is ${it.latitude}, longitude is ${it.longitude}")
                                    if(getCity != null){
                                        Repository.citynow = getCity
                                    }
                                    if (Repository.cityname == null) {
                                        Repository.cityname = Repository.citynow
                                        viewModel.fresh(requireActivity())
                                    }
                                }
                            }
                        }
                    }else{
                        Toast.makeText(requireContext(), R.string.no_network_connection, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun locationRefresh(){
            location()
            locationPermission.requestLocationUpdates { location ->
                location.let {
                    if(activity != null && isAdded){
                    getCity = Repository().getCityName(it.latitude, it.longitude, requireActivity())
                    //Log.d("preferenceChange", " $getCity latitude is ${it.latitude}, longitude is ${it.longitude}")
                    if(getCity != null){
                        Repository.citynow = getCity
                    }
                    if (Repository.cityname == null) {
                        Repository.cityname = Repository.citynow
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun refresh(){
        viewModel.fresh(requireActivity())
    }


    @SuppressLint("NotifyDataSetChanged")
    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainMeteoViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val imageView = view.findViewById<ImageView>(R.id.ImageMain)
        viewModel.weatherImageResourceId.observe(viewLifecycleOwner) { imageResourceId ->
            // Aggiornare l'imagine
            imageView.setImageResource(imageResourceId)
        }

        //Next Hours
        val weatherNextHourAdapter = WeatherNextHour(requireContext())
        binding.recyclerViewNexHour.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewNexHour.adapter = weatherNextHourAdapter


        // Observe les changements dans la liste de prévisions météorologiques
        viewModel.weatherNexHour.observe(viewLifecycleOwner) { weatherList ->
            weatherNextHourAdapter.setForecastList(weatherList)
        }

        //Next Days
        val weatherNextDaysAdapter = WeatherNextDays()
        binding.recyclerviewNexDay.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewNexDay.adapter = weatherNextDaysAdapter
        viewModel.finalListNextDay.observe(viewLifecycleOwner) { finalListNextDay ->
            weatherNextDaysAdapter.setForecastList(finalListNextDay)
        }

    }

        @RequiresApi(Build.VERSION_CODES.S)
        override fun onResume() {
        super.onResume()
        val toolbarTitle = (requireActivity() as AppCompatActivity).findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = null
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun swipeRefresh(){
        val swipe = (activity as AppCompatActivity).findViewById<SwipeRefreshLayout>(R.id.swiperefreshlayout)
        swipe.setOnRefreshListener {
            locationRefresh()
            if (activity?.let { Repository().isNetworkAvailable(it) } == true){
                //Log.d("city value", "${Repository.cityname}")
                viewModel.getWeather()
                lifecycleScope.launch {
                    viewModel.getWeatherNextDays()
                    viewModel.getWeatherNexHour()
                }
            }else{
                Toast.makeText(requireContext(), R.string.no_network_connection, Toast.LENGTH_SHORT).show()
            }
            swipe.isRefreshing = false
        }
    }

}


