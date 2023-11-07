package com.example.meteoapp.mainMeteo

import LocationPermission
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import com.example.meteoapp.adapter.WeatherNextDays
import com.example.meteoapp.adapter.WeatherNextHour
import com.example.meteoapp.adapter.WeatherToday
import com.example.meteoapp.databinding.FragmentMainMeteoBinding
import kotlinx.coroutines.launch
import java.util.*

class MainMeteoFragment : Fragment() {

    //lateinit var adapter: WeatherToday
    var lat: String = ""
    var lon: String = ""
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

        locationPermission = LocationPermission(requireActivity())
        viewModel.setLocationPermission(locationPermission)

        locationPermission.requestLocationPermission { granted ->
            if (granted) {
                locationPermission.requestLocationUpdates { location ->
                    location?.let {
                        viewModel.getWeather(it.latitude, it.longitude)
                        setupRecyclerView()
                    } ?: kotlin.run {
                        Toast.makeText(requireContext(), "Impossible recuperare la localisazione", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Permissione non accettata", Toast.LENGTH_SHORT).show()
            }
        }

        fresh()
        swipeRefresh()
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        locationPermission.onRequestPermissionsResult(requestCode, grantResults) { granted ->
            if (granted) {
                locationPermission.requestLocationUpdates { location ->
                    location?.let {
                        if (isNetworkAvailable(requireContext())) {
                            viewModel.getWeather(it.latitude, it.longitude)
                        } else {
                            showToast(R.string.no_network_connection)
                        }
                    } ?: kotlin.run {
                        showToast(R.string.location_unavailable)
                    }
                }
            } else {
                showToast(R.string.permission_not_granted)
            }
        }
    }

    private fun showToast(messageResId: Int) {
        Toast.makeText(requireContext(), getString(messageResId), Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("NotifyDataSetChanged")
    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainMeteoViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val imageView = view.findViewById<ImageView>(R.id.ImageMain)
        viewModel.weatherImageResourceId.observe(viewLifecycleOwner) { imageResourceId ->
            // Mise à jour l'image
            imageView.setImageResource(imageResourceId)
        }

        //Next Hours
        val weatherNextHourAdapter = WeatherNextHour()
        binding.recyclerViewNexHour.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
        binding.recyclerviewNexDay.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewNexDay.adapter = weatherNextDaysAdapter

        // Appele la fonction getWeatherNextDays à l'intérieur de la coroutine
        lifecycleScope.launch {
            viewModel.getWeatherNextDays()
        }

        // Observe les changements dans la liste de prévisions météorologiques pour les prochains jours
        viewModel.weatherNextDays.observe(viewLifecycleOwner) { weatherList ->
            weatherNextDaysAdapter.setForecastList(weatherList)
        }
    }

        override fun onResume() {
        super.onResume()
        val toolbarTitle =
            (requireActivity() as AppCompatActivity).findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = null
    }

    private fun setupRecyclerView() {
        binding.recyclerviewNexDay.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = WeatherToday()
        }
    }

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

    @RequiresApi(Build.VERSION_CODES.S)
    private fun swipeRefresh() {
        val swipe = (activity as AppCompatActivity).findViewById<SwipeRefreshLayout>(R.id.swiperefreshlayout)
        swipe.setOnRefreshListener {
            locationPermission.requestLocationUpdates { location ->
                location?.let {
                    if (activity?.let { isNetworkAvailable(it) } == true) {
                        viewModel.getWeather(it.latitude, it.longitude)
                    } else {
                        Toast.makeText(requireContext(), "There is no network connection", Toast.LENGTH_SHORT).show()
                    }
                } ?: kotlin.run {
                    Toast.makeText(requireContext(), "Impossible recuperare la localisazione", Toast.LENGTH_SHORT).show()
                }
            }
            swipe.isRefreshing = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun fresh() {
        if (viewModel != null) {
            locationPermission.requestLocationUpdates { location ->
                location?.let {
                    if (activity?.let { isNetworkAvailable(it) } == true) {
                        viewModel.getWeather(it.latitude, it.longitude)
                        setupRecyclerView()
                    } else {
                        Toast.makeText(requireContext(), "There is no network connection", Toast.LENGTH_SHORT).show()
                    }
                } ?: kotlin.run {
                    Toast.makeText(requireContext(), "Impossible recuperare la localisazione", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}


