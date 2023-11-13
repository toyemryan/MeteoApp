package com.example.meteoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meteoapp.PlaceActivity
import com.example.meteoapp.R
import com.google.android.libraries.places.api.model.Place

class CityAdapter(private val cityList: List<Place>, private val clickListener: PlaceActivity) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val cityName: TextView = itemView.findViewById(R.id.cityName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return cityList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCity = cityList[position]
        holder.cityName.text = currentCity.name

        //Détecte le long click
        holder.itemView.setOnClickListener{
            clickListener.onCityLongClick(currentCity)
            true
        }
    }

}

interface CityItemClickListener{
    fun onCityLongClick(place: Place)
}