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

    fun saveCityName(cityname : String?){
        val editor = sharedPreferences!!.edit()
        // Salva la lista sérialisato dentro le scharedPreferences
        editor.putString("cityName", cityname)
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

    fun loadCityName(): String? {
        return sharedPreferences?.getString("cityName", null)
    }

    fun saveNotificationState(enabled: Boolean) {
        val editor = sharedPreferences!!.edit()
        editor.putBoolean("notification_enabled", enabled)
        editor.apply()
    }

    fun isNotificationEnabled(): Boolean {
        return sharedPreferences?.getBoolean("notification_enabled", false) ?: false
    }

}
