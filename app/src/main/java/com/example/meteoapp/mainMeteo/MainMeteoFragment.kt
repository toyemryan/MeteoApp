package com.example.meteoapp.mainMeteo

//import WeatherFiveDayAdapter
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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.meteoapp.R
import com.example.meteoapp.adapter.WeatherNextHour
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
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainMeteoViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val imageView = view.findViewById<ImageView>(R.id.ImageMain)
        viewModel.weatherImageResourceId.observe(viewLifecycleOwner) { imageResourceId ->
            // Mise à jour l'image
            imageView.setImageResource(imageResourceId)
        }

       val weatherNextHourAdapter = WeatherNextHour()

        // Utilisez le fichier de liaison pour accéder à la vue recyclerViewFiveday
        binding.recyclerViewNexHour.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerViewNexHour.adapter = weatherNextHourAdapter
        viewModel.getWeatherNexHour()

        // Observer les changements dans la liste de prévisions météorologiques
        viewModel.weatherNexHour.observe(viewLifecycleOwner) { weatherList ->
            weatherNextHourAdapter.setForecastList(weatherList)
            weatherNextHourAdapter.notifyDataSetChanged()
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
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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

        val swipe = (activity as AppCompatActivity).findViewById<SwipeRefreshLayout>(R.id.swiperefreshlayout)
        swipe.isRefreshing = true

        if (activity?.let { isNetworkAvailable(it) } == true){
            viewModel.getWeather()
            setupRecyclerView()
            swipe.isRefreshing = false
        }else{
            Toast.makeText(requireContext(), "There is no network connection", Toast.LENGTH_SHORT).show()
            swipe.isRefreshing = false
        }
    }

}


