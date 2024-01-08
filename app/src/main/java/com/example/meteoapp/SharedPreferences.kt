package com.example.meteoapp

import android.content.Context
import android.content.SharedPreferences
import com.google.android.libraries.places.api.model.Place
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences? = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    fun saveCityList(cityList: List<Place>){
        val editor = sharedPreferences!!.edit()
        // Convertire la lista delle città in una lista sérialisato di SerializablePlace
        val serializableList = cityList.map { SerializablePlace(it) }
        // Salva la lista sérialisato dentro le scharedPreferences
        editor.putString("cityList", Gson().toJson(serializableList))
        editor.apply()
    }


    // Funzione per caricare una lista di città dalle preferenze condivise
    fun loadCityList(): List<Place> {
        val savedCityList = sharedPreferences?.getString("cityList", null)
        return  if (savedCityList != null){
            // Deserializzazione della lista serializzata in una lista di SerializablePlace
            val type = object : TypeToken<List<SerializablePlace>>(){}.type
            val serializableList = Gson().fromJson<List<SerializablePlace>>(savedCityList, type)
            // Mappatura della lista di SerializablePlace a una lista di Place
            serializableList.map { Place.builder().setName(it.name).setId(it.id).setAddress(it.address).build() }
        }else{
            emptyList()
        }
    }


}
