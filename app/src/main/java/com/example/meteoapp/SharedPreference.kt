package com.example.meteoapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences


class SharedPreference internal constructor(private val context: Context)  {

    // Objet compagnon contenant des constantes pour les clés des préférences partagées
    companion object {
        private const val SHARED_PREFERENCES_NAME = "my_preference"  // Nom du fichier de préférences partagées
        private const val CITY_KEY = "city"  // Clé pour stocker la valeur de la ville dans les préférences partagées


        // Instance unique de la classe SharedPrefs
        @SuppressLint("StaticFieldLeak")
        private var instance: SharedPreference? = null

        // Méthode statique pour obtenir l'instance unique de SharedPrefs
        fun getInstance(context: Context) :SharedPreference{
            if (instance == null){
                instance = SharedPreference(context.applicationContext) // Création de l'instance SharedPrefs si elle est nulle
            }
            return instance !!
        }
    }

    // Propriété paresseuse pour accéder aux préférences partagées de l'application
    private val preference: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }


    // Méthode pour enregistrer une valeur dans les préférences partagées
    fun setValue(key: String , value: String?){
        preference.edit().putString(key, value).apply()
    }

    fun getValue(key: String): String? {
        return preference.getString(key, null)
    }

    fun setValueOrNull(key: String?, value: String?) {
        if (key != null && value != null) {
            preference.edit().putString(key, value).apply()
        }
    }

    fun getValueOrNull(key: String?): String? {
        if (key != null) {
            return preference.getString(key, null)
        }
        return null
    }



    // Méthode pour supprimer la valeur associée à la clé de la ville des préférences partagées
    fun clearCityValue() {
        preference.edit().remove(CITY_KEY).apply()
    }

}
