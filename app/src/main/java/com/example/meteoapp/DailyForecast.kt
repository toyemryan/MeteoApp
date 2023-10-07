package com.example.meteoapp

class DailyForecast (
    val date: String, // La date de la prévision
    val temperature: Int, // La température prévue pour ce jour
    val description: String, // Une description de la météo (ensoleillé, pluvieux, etc.)
    val weatherIconResId: Int // L'ID de la ressource de l'icône météorologique associée à cette prévision
) {


}