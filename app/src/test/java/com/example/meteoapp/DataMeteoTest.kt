package com.example.meteoapp

import com.example.meteoapp.service.RetrofitInstance
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class DataMeteoTest {

   // lateinit var getFromLocation :  MutableList<List<Address>?>

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private var City = String()

    /**Test per verificare che effetivamente l'API ci torna 40 elementi di meteo.
     * informazione meteo in intervallo di 3 ore
     */
    @Test
    fun nextDay(): Unit = runBlocking {

        launch(Dispatchers.Main) {  // Will be launched in the mainThreadSurrogate dispatcher

                val call =  City.let { RetrofitInstance.api.getCurrentWeatherByCity(it) }
                val response = call.execute()

            assertEquals(response.body()?.weatherList?.size, 40)
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        City = "Ancona"
        Dispatchers.setMain(mainThreadSurrogate)

    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

}