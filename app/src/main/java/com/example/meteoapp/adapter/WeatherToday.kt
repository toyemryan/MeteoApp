package com.example.meteoapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meteoapp.R
import com.lionel.mameteo.modal.WeatherList
import java.text.SimpleDateFormat
import java.util.Calendar

class WeatherToday : RecyclerView.Adapter<TodayHolder>() {

    // Lista delle previsioni meteorologiche di oggi
    private var listOfTodayWeather = listOf<WeatherList>()

    // Metodo chiamato quando viene creato un nuovo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayHolder {
        // Infla il layout dell'elemento dell'elenco delle previsioni orarie di oggi
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_today, parent, false)
        return TodayHolder(view)
    }


    // Metodo chiamato per visualizzare i dati in una posizione specifica
    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: TodayHolder, position: Int) {
        val todayForeCast = listOfTodayWeather[position]

        // Imposta l'ora nel formato HH:mm dalla data e ora fornite dalle previsioni meteorologiche
        holder.timeDisplay.text = todayForeCast.dtTxt!!.substring(11, 16).toRegex().toString()


        // Converte la temperatura da Fahrenheit a Celsius e formatta il risultato
        val temperatureFahrenheit = todayForeCast.main?.temp
        val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
        val temperatureFormatted = String.format("%.2f", temperatureCelsius)

        holder.tempDisplay.text = "$temperatureFormatted °C"

        // Ottiene l'orario attuale nel formato HH:mm
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm")
        val formattedTime = dateFormat.format(calendar.time)


        // Estrae l'orario dalle previsioni meteorologiche fornite dall'API


        // Logga l'orario attuale e l'orario dalle previsioni meteorologiche


        Log.e("time" , " formatted time:${formattedTime}")

        // Imposta l'icona del tempo in base al codice fornito dalle previsioni meteorologiche
        for (i in todayForeCast.weather){
            when (i.icon){
                "01d" -> holder.imageDisplay.setImageResource(R.drawable.oned)
                "01n" -> holder.imageDisplay.setImageResource(R.drawable.onen)
                "02d" -> holder.imageDisplay.setImageResource(R.drawable.twod)
                "02n" -> holder.imageDisplay.setImageResource(R.drawable.twon)
                "03d", "03n" -> holder.imageDisplay.setImageResource(R.drawable.threedn)
                "10d" -> holder.imageDisplay.setImageResource(R.drawable.tend)
                "10n" -> holder.imageDisplay.setImageResource(R.drawable.tenn)
                "04d", "04n" -> holder.imageDisplay.setImageResource(R.drawable.fourdn)
                "09d", "09n" -> holder.imageDisplay.setImageResource(R.drawable.ninedn)
                "11d", "11n" -> holder.imageDisplay.setImageResource(R.drawable.elevend)
                "13d", "13n" -> holder.imageDisplay.setImageResource(R.drawable.thirteend)
                "50d", "50n" -> holder.imageDisplay.setImageResource(R.drawable.fiftydn)
            }
        }

    }


    // Metodo per impostare la lista delle previsioni meteorologiche di oggi nell'adapter
    fun setList(listOfToday: List<WeatherList>){
        this.listOfTodayWeather = listOfToday
    }


    // Restituisce il numero totale di elementi nell'elenco delle previsioni meteorologiche di oggi
    override fun getItemCount(): Int {
        return listOfTodayWeather.size
    }
}


// ViewHolder per visualizzare un elemento dell'elenco delle previsioni meteorologiche di oggi
class TodayHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    // ImageView per visualizzare l'icona del tempo
    val imageDisplay: ImageView = itemView.findViewById(R.id.imagetemptoday)

    // TextView per visualizzare la temperatura
    val tempDisplay: TextView = itemView.findViewById(R.id.temperaturetoday)

    // TextView per visualizzare l'orario
    val timeDisplay: TextView = itemView.findViewById(R.id.hourstoday)
}

