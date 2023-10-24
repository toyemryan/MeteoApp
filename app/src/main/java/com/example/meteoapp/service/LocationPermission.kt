package com.example.meteoapp.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.location.Location
import android.Manifest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient


class LocationPermission (private val context: Context){

    // Inizializzazione di FusedLocationProviderClient utilizzando LocationServices per ottenere l'ultima posizione conosciuta del dispositivo
    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    // Funzione che restituisce true se il permesso di localizzazione è concesso, altrimenti false
    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // Annotazione per sopprimere l'avviso di mancanza di permesso
    @SuppressLint("MissingPermission")

    // Funzione per richiedere gli aggiornamenti di localizzazione, accettando un blocco di codice come parametro
    fun requestLocationUpdates(locationListener: (Location) -> Unit){
        if (isLocationPermissionGranted()){
            fusedLocationClient.lastLocation.addOnSuccessListener {
                location:Location ->

                // Verifica se la posizione non è nulla e invoca il blocco di codice locationListener
                location?.let {
                    locationListener.invoke(it)
                }
            }
        }
    }

}

