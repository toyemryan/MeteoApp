package com.example.meteoapp

import android.content.Context
import android.content.SharedPreferences
import com.google.android.libraries.places.api.model.Place
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences? =
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    fun saveCityList(cityList: List<Place>) {
        val currentCityList = loadCityList().toMutableSet()

        //Metode pour supprimer les villes qui ne sont plus dans la liste affichée
        val citiesToRemove = currentCityList.filter { city -> !cityList.contains(city) }
        currentCityList.removeAll(citiesToRemove)

        //Metode pour ajouter les nouvelles villes
        cityList.forEach { city ->
            currentCityList.add(city)
        }


        val editor = sharedPreferences!!.edit()
        val serializableList = currentCityList.map { SerializablePlace(it) }
        editor.putString("cityList", Gson().toJson(serializableList))
        editor.apply()
    }

    fun loadCityList(): List<Place> {
        val savedCityList = sharedPreferences?.getString("cityList", null)
        return if (savedCityList != null) {
            val type = object : TypeToken<List<SerializablePlace>>() {}.type
            val serializableList = Gson().fromJson<List<SerializablePlace>>(savedCityList, type)
            serializableList.map { Place.builder().setName(it.name).setId(it.id).setAddress(it.address).build() }
        } else {
            emptyList()
        }
    }
}
