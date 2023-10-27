package com.example.meteoapp.mainMeteo

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.meteoapp.R
import com.example.meteoapp.adapter.WeatherToday
import com.example.meteoapp.databinding.FragmentMainMeteoBinding
import com.example.meteoapp.service.LocationPermission
import java.util.*


class MainMeteoFragment : Fragment() {


    lateinit var adapter: WeatherToday
    var lat: String = ""
    var lon: String = ""
    private lateinit var locationPermission: LocationPermission
    private lateinit var binding: FragmentMainMeteoBinding

    private val viewModel: MainMeteoViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_meteo, container, false)

        Fresh()
        swipeRefresh()

        return binding.root
    }

    /* @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")*/
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainMeteoViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.day.observe(viewLifecycleOwner) { day ->
            view.findViewById<TextView>(R.id.today)?.text = "$day"
        }

        viewModel.hour.observe(viewLifecycleOwner) { hour ->
            view.findViewById<TextView>(R.id.hour)?.text = "$hour"
        }


        viewModel.feelLike.observe(viewLifecycleOwner) { feelLike ->
            view.findViewById<TextView>(R.id.feel)?.text = "Feels Like: $feelLike"
        }


        viewModel.windSpeed.observe(viewLifecycleOwner) { windSpeed ->
            view.findViewById<TextView>(R.id.windSpeed)?.text = "$windSpeed km/s"
        }


        viewModel.humidity.observe(viewLifecycleOwner) { humidity ->
            view.findViewById<TextView>(R.id.humidity)?.text = "$humidity"
        }


        //viewModel.weatherImageResourceId.observe(viewLifecycleOwner, { imageResourceId ->
         //   view.findViewById<ImageView>(R.id.ImageMain)?.setImageResource(imageResourceId)
        //})


        viewModel.pressure.observe(viewLifecycleOwner) { pressure ->
            view.findViewById<TextView>(R.id.pressure)?.text = "$pressure"
        }


        viewModel.weatherCondition.observe(viewLifecycleOwner) { weatherCondition ->
            view.findViewById<TextView>(R.id.condition)?.text = "$weatherCondition"
        }


        viewModel.minTemp.observe(viewLifecycleOwner) { minTemp ->
            view.findViewById<TextView>(R.id.tempMin)?.text = "$minTemp"
        }


        viewModel.maxTemp.observe(viewLifecycleOwner) { maxTemp ->
            view.findViewById<TextView>(R.id.tempMax)?.text = "$maxTemp"
        }


        viewModel.seaLevel.observe(viewLifecycleOwner) { seaLevel ->
            view.findViewById<TextView>(R.id.seaLevel)?.text = "$seaLevel"
        }


        viewModel.sunriseTime.observe(viewLifecycleOwner) { sunriseTime ->
            view.findViewById<TextView>(R.id.sunr)?.text = "$sunriseTime"
        }

        viewModel.sunsetTime.observe(viewLifecycleOwner) { sunsetTime ->
            view.findViewById<TextView>(R.id.suns)?.text = "$sunsetTime"
        }


        viewModel.pressureImageId.observe(viewLifecycleOwner) { imageResId ->
            view.findViewById<ImageView>(R.id.impressure)?.setImageResource(imageResId)
        }

        viewModel.humidityImageId.observe(viewLifecycleOwner) { imageResId ->
            view.findViewById<ImageView>(R.id.imhumidity)?.setImageResource(imageResId)
        }

        viewModel.windSpeedImageId.observe(viewLifecycleOwner) { imageResId ->
            view.findViewById<ImageView>(R.id.imWin)?.setImageResource(imageResId)
        }


    }
    override fun onResume() {
        super.onResume()
        val toolbarTitle =
            (requireActivity() as AppCompatActivity).findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = null
    }

    private fun setupRecyclerView() {
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = WeatherToday()
        }
    }


    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw      = connectivityManager.activeNetwork ?: return false
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
            setupRecyclerView()
        }else{
            Toast.makeText(requireContext(), "There is no network connection", Toast.LENGTH_SHORT).show()
        }
        //val swipe = (activity as AppCompatActivity).findViewById<SwipeRefreshLayout>(R.id.swiperefreshlayout)
        swipe.isRefreshing = false
    }
}

    private fun Fresh(){
        if (activity?.let { isNetworkAvailable(it) } == true){
            viewModel.getWeather()
            setupRecyclerView()
        }else{
            Toast.makeText(requireContext(), "There is no network connection", Toast.LENGTH_SHORT).show()
        }
    }
}


