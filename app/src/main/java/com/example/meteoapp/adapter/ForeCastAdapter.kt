package com.example.meteoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.meteoapp.R
import com.lionel.mameteo.modal.WeatherList

// Adapter per visualizzare le previsioni meteorologiche nell'elenco delle prossime previsioni
class ForeCastAdapter(forecastObject: Any) : RecyclerView.Adapter<ForeCastHolder>() {
    // Lista delle previsioni meteorologiche
    private var listofforecast = listOf<WeatherList>()

    // Metodo chiamato quando viene creato un nuovo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForeCastHolder {
        // Infla il layout dell'elemento dell'elenco delle previsioni future
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fiveday_forecast, parent, false)
        return ForeCastHolder(view)
    }

    override fun getItemCount(): Int {
        return listofforecast.size
    }

    override fun onBindViewHolder(holder: ForeCastHolder, position: Int) {
        // Ottiene le previsioni meteorologiche per la posizione specifica nell'elenco
        val forecastObject = listofforecast[position]

        // Imposta la descrizione del tempo dalle previsioni meteorologiche
        for (i in forecastObject.weather) {
            holder.description.text = i.description!!
        }
        // Imposta umidità e velocità del vento dalle previsioni meteorologiche
        holder.humiditiy.text = forecastObject.main!!.humidity.toString()
        holder.windspeed.text = forecastObject.wind?.speed.toString()

        // Converte la temperatura da Fahrenheit a Celsius e formatta il risultato
        val temperatureFahrenheit = forecastObject.main?.temp
        val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
        val temperatureFormatted = String.format("%.2f", temperatureCelsius)
        holder.temp.text = "$temperatureFormatted °C"

        // Formatta la data e il giorno dalle previsioni meteorologiche


        // Imposta l'icona del tempo in base al codice fornito dalle previsioni meteorologiche
        for (i in forecastObject.weather) {
            when (i.icon) {
                "01d" -> {
                    holder.imageGraphic.setImageResource(R.drawable.oned)
                    holder.smallIcon.setImageResource(R.drawable.oned)
                }

                "01n" -> {
                    holder.imageGraphic.setImageResource(R.drawable.onen)
                    holder.smallIcon.setImageResource(R.drawable.onen)
                }

                "02d" -> {
                    holder.imageGraphic.setImageResource(R.drawable.twod)
                    holder.smallIcon.setImageResource(R.drawable.twod)
                }

                "02n" -> {
                    holder.imageGraphic.setImageResource(R.drawable.twon)
                    holder.smallIcon.setImageResource(R.drawable.twon)
                }

                "03d", "03n" -> {
                    holder.imageGraphic.setImageResource(R.drawable.threedn)
                    holder.smallIcon.setImageResource(R.drawable.threedn)
                }

                "10d" -> {
                    holder.imageGraphic.setImageResource(R.drawable.tend)
                    holder.smallIcon.setImageResource(R.drawable.tend)
                }

                "10n" -> {
                    holder.imageGraphic.setImageResource(R.drawable.tenn)
                    holder.smallIcon.setImageResource(R.drawable.tenn)
                }

                "04d", "04n" -> {
                    holder.imageGraphic.setImageResource(R.drawable.fourdn)
                    holder.smallIcon.setImageResource(R.drawable.fourdn)
                }

                "09d", "09n" -> {
                    holder.imageGraphic.setImageResource(R.drawable.ninedn)
                    holder.smallIcon.setImageResource(R.drawable.ninedn)
                }

                "11d", "11n" -> {
                    holder.imageGraphic.setImageResource(R.drawable.elevend)
                    holder.smallIcon.setImageResource(R.drawable.elevend)
                }

                "13d", "13n" -> {
                    holder.imageGraphic.setImageResource(R.drawable.thirteend)
                    holder.smallIcon.setImageResource(R.drawable.thirteend)
                }

                "50d", "50n" -> {
                    holder.imageGraphic.setImageResource(R.drawable.fiftydn)
                    holder.smallIcon.setImageResource(R.drawable.fiftydn)
                }
            }
        }
    }
}


class ForeCastHolder(itemView: View) : ViewHolder(itemView) {
    // ImageView per visualizzare l'icona del tempo
    val imageGraphic: ImageView = itemView.findViewById(R.id.imageGraphic)

    // TextView per visualizzare la descrizione del tempo
    val description: TextView = itemView.findViewById(R.id.weatherDescr)

    // TextView per visualizzare l'umidità
    val humiditiy: TextView = itemView.findViewById(R.id.humidity)

    // TextView per visualizzare la velocità del vento
    val windspeed: TextView = itemView.findViewById(R.id.windSpeed)

    // TextView per visualizzare la temperatura
    val temp: TextView = itemView.findViewById(R.id.tempDisplayForeCast)

    // ImageView per visualizzare l'icona del tempo in piccolo
    val smallIcon: ImageView = itemView.findViewById(R.id.smallIcon)

    // TextView per visualizzare la data e il giorno
    val dateDayName: TextView = itemView.findViewById(R.id.dayDateText)
}
