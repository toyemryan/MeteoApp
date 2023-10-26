package com.example.meteoapp.mainMeteo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meteoapp.R
import com.example.meteoapp.adapter.WeatherToday
import com.example.meteoapp.databinding.FragmentMainMeteoBinding
import com.example.meteoapp.service.LocationPermission
import com.example.meteoapp.service.RetrofitInstance
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale


class MainMeteoFragment : Fragment() {

    //private lateinit var response : Response<ForeCast>

    //var weatherTodayList = mutableListOf<WeatherList>()

    //private lateinit var viewModel: MainMeteoViewModel
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

        viewModel.getWeather()
        setupRecyclerView()

        return binding.root
    }

    /* @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")*/
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainMeteoViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Inizializzazione del viewModel utilizzando ViewModelProvider
        //  viewModel = ViewModelProvider(this)[MainMeteoViewModel::class.java]

        /* locationPermission = LocationPermission(requireContext())

         // Richiesta di aggiornamenti sulla posizione se il permesso è già concesso
         // Altrimenti, richiesta del permesso di localizzazione
         if (locationPermission.isLocationPermissionGranted()) {
             requestLocationUpdates()
         } else {
             ActivityCompat.requestPermissions(
                 requireActivity(),
                 arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                 Utility.LOCATION_PERMISSION_REQUEST_CODE
             )
         }

        // Inizializzazione dell'adapter per la RecyclerView
       // adapter = WeatherToday()


        viewModel.todayWeatherLiveData.observe(viewLifecycleOwner, Observer {
            val setNewList = it as List<WeatherList>
            Log.e("Todayweater list", it.toString())
            adapter.setList(setNewList)
            binding.recyclerView1.adapter = adapter


            private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount // pour linker et dans le layout on met @{MainMeteoViewModel.currentScrambledWord}"


        })

       // binding.temperature.text = response.body()!!.weatherList[0].main?.temp.toString()

        // Osservazione dei dati meteorologici più vicini e aggiornamento dell'interfaccia utente quando cambiano
        viewModel.closetorexactlysameweatherdata.observe(viewLifecycleOwner) {
            val temperatureFahreheit = it!!.main?.temp
            val temperatureCelsius = (temperatureFahreheit?.minus(273.15))
            val temperatureFormatted = String.format("%.2f", temperatureCelsius)

            // Iterazione attraverso i dati meteorologici per ottenere le informazioni necessarie
            for (i in it.weather) {
                binding.tempffectif.text = i.description

                // Notifica se le condizioni meteorologiche sono pioggia, nuvoloso, temporale o sereno
                if (i.main.toString() == "Rain" ||
                    i.main.toString() == "Clouds" ||
                    i.main.toString() == "Drizzle" ||
                    i.main.toString() == "Thunderstorm" ||
                    i.main.toString() == "Clear" ||
                    i.main.toString() == "snow"
                ) {
                    Log.e("MAIN", i.main.toString())
                }
            }

            // Aggiornamento delle view con i dati meteorologici
            binding.temperature.text = "$temperatureFormatted°"
            binding.feel.text = "$temperatureFormatted°"
            binding.rain.text = it.weather.toString()
            binding.windSpeed.text = it.wind?.speed.toString()
            binding.humidity.text = it.main!!.humidity.toString()

            // Formattazione della data e del giorno
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val date = it.dtTxt?.let { it1 -> inputFormat.parse(it1) }
            val outputFormat = SimpleDateFormat("d MMMM EEEE", Locale.getDefault())
            val dateAndDay = outputFormat.format(date!!)

            binding.dateoftheday.text = dateAndDay
            binding.rain.text = "${it.main.toString()}%"

            // Impostazione dell'icona in base al codice dell'icona ricevuto dalla risposta API
            for (i in it.weather) {
                when (i.icon) {
                    "01d" -> binding.ImageMain.setImageResource(R.drawable.threedn)
                    "01n" -> binding.ImageMain.setImageResource(R.drawable.threedn)
                    "02d" -> binding.ImageMain.setImageResource(R.drawable.cloud)
                    "02n" -> binding.ImageMain.setImageResource(R.drawable.cloudy)
                    "03d", "03n" -> binding.ImageMain.setImageResource(R.drawable.cloudy_sunny)
                    "09d", "09n" -> binding.ImageMain.setImageResource(R.drawable.rainy)
                    "10d" -> binding.ImageMain.setImageResource(R.drawable.rainy)
                    "10n" -> binding.ImageMain.setImageResource(R.drawable.rain)
                    "13d", "13n" -> binding.ImageMain.setImageResource(R.drawable.thirteend)
                }
            }
        }*/
    }

    /*private fun requestLocationUpdates() {
        locationPermission.requestLocationUpdates { location ->
            val latitude = location.latitude
            val longitude = location.longitude

            viewModel.getWeather(null, latitude.toString(), longitude.toString())
            logLocation(latitude, longitude)
        }
    }

    private fun logLocation(latitude: Double, longitude: Double) {
        val message = "Latitude: $latitude, Longitude: $longitude"
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    } */

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



}


