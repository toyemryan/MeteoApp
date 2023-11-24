package com.example.meteoapp.mainMeteo

import LocationPermission
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.meteoapp.R
import com.example.meteoapp.adapter.WeatherNextDays
import com.example.meteoapp.adapter.WeatherNextHour
import com.example.meteoapp.databinding.FragmentMainMeteoBinding
import kotlinx.coroutines.launch
import java.util.*

class MainMeteoFragment : Fragment() {

    //lateinit var adapter: WeatherToday
    //var lat: String = ""
    //var lon: String = ""
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
        locationPermission = LocationPermission(requireContext()) // LocationPermission(requireActivity())
        viewModel.setLocationPermission(locationPermission)

        locationPermission.requestLocationPermission { granted ->
            if (granted) {
                locationPermission.requestLocationUpdates { location ->
                    location?.let {
                       // Log.d("preferenceChange", "latitude is ${it.latitude}, longitude is ${it.longitude}")
                        Log.d("preferenceChange", " ${getCityName(it.latitude, it.longitude)} latitude is ${it.latitude}, longitude is ${it.longitude}")
                    }
                }
            }
        }

        fresh()
        swipeRefresh()
        return binding.root
    }

    private fun getCityName(lat: Double,long: Double): String? {
        val cityName: String?
        val geoCoder = context?.let { Geocoder(it, Locale.getDefault()) }
        val adress = geoCoder?.getFromLocation(lat,long,3)

        cityName = adress?.get(0)?.locality
       // Log.d("preferenceChange", " $adress")
        return cityName
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        locationPermission.onRequestPermissionsResult(requestCode, grantResults) { granted ->
            if (granted) {
                locationPermission.requestLocationUpdates { location ->
                    location.let {
                        Log.d("preferenceChange", "2 latitude is ${it.latitude}, longitude is ${it.longitude}")
                    }
                }
            }
        }
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
        val weatherNextHourAdapter = WeatherNextHour()
        binding.recyclerViewNexHour.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewNexHour.adapter = weatherNextHourAdapter

        // Appele la fonction getWeatherNextDays à l'intérieur de la coroutine
        lifecycleScope.launch {
            viewModel.getWeatherNexHour()
        }

        // Observe les changements dans la liste de prévisions météorologiques
        viewModel.weatherNexHour.observe(viewLifecycleOwner) { weatherList ->
            weatherNextHourAdapter.setForecastList(weatherList)
            weatherNextHourAdapter.notifyDataSetChanged()
        }


        //Next Days
        val weatherNextDaysAdapter = WeatherNextDays()
        binding.recyclerviewNexDay.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewNexDay.adapter = weatherNextDaysAdapter

        // Appele la fonction getWeatherNextDays à l'intérieur de la coroutine
        lifecycleScope.launch {
            viewModel.getWeatherNextDays()
        }
        // Observe les changements dans la liste de prévisions météorologiques pour les prochains jours
        viewModel.weatherNextDays.observe(viewLifecycleOwner) { weatherList ->
            weatherNextDaysAdapter.setForecastList(weatherList)
        }
        viewModel.cityName.observe(viewLifecycleOwner) { cityName ->
            Log.d("Fragment", "City Name Updated: $cityName")
            binding.cityName.text = cityName
        }
    }

        override fun onResume() {
        super.onResume()
        val toolbarTitle = (requireActivity() as AppCompatActivity).findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = null
    }

   /* private fun setupRecyclerView() {
        binding.recyclerviewNexDay.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = WeatherToday()
        }
    }*/

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    private fun swipeRefresh(){
        val swipe = (activity as AppCompatActivity).findViewById<SwipeRefreshLayout>(R.id.swiperefreshlayout)
        swipe.setOnRefreshListener {
            if (activity?.let { isNetworkAvailable(it) } == true){
                viewModel.getWeather()
                //setupRecyclerView()
            }else{
                Toast.makeText(requireContext(), R.string.error_network, Toast.LENGTH_SHORT).show()
            }
            //val swipe = (activity as AppCompatActivity).findViewById<SwipeRefreshLayout>(R.id.swiperefreshlayout)
            swipe.isRefreshing = false
        }
    }
    private fun fresh(){
        if (activity?.let { isNetworkAvailable(it) } == true){
            viewModel.getWeather()
            //setupRecyclerView()
        }else{
            Toast.makeText(requireContext(), R.string.error_network, Toast.LENGTH_SHORT).show()
        }
    }

}


