package com.example.meteoapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meteoapp.adapter.CityAdapter
import com.example.meteoapp.databinding.ActivityPlaceBinding
import com.example.meteoapp.repository.Repository
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class  PlaceActivity : AppCompatActivity() {

      private lateinit var binding: ActivityPlaceBinding
      private lateinit var startAutocomplete: ActivityResultLauncher<Intent>

      private val cityList:MutableList<Place> = mutableListOf()
      private lateinit var recyclerView: RecyclerView
      private lateinit var cityAdapter: CityAdapter

      private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_place)

        binding = ActivityPlaceBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar2)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val apiKey = getString(R.string.api_key)

        if (!Places.isInitialized()) {
            Places.initialize(this, apiKey)
        }
        // Create a new Places client instance.
        val placesClient = Places.createClient(this)


        recyclerView = binding.recyclerviewCity
        recyclerView.layoutManager = LinearLayoutManager(this)
        cityAdapter = CityAdapter(cityList, this)
        recyclerView.adapter = cityAdapter

        //Inizialise SharedPreferences
        sharedPreferences = SharedPreferences(this)

        //inieta i date salvati dalla classe SharedPreferences
        cityList.addAll(sharedPreferences.loadCityList())
        cityAdapter.notifyDataSetChanged()

        onSearchCalled()

        //setContentView(binding.root)
    }

   fun onCityLongClick(place: Place){
        showConfirmationDialog(place)
        //removeCity(place)
    }

    fun onCityClick(place: Place){
        Repository.cityname = place.name?.toString()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showConfirmationDialog(place: Place) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
            .setMessage("Sei sicuro di voler cancelare la città di ${place.name} ?")
            .setPositiveButton("si"){_, _ -> removeCity(place)}
            .setNegativeButton("No"){dialog, _ -> dialog.dismiss()}.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeCity(place: Place) {
        cityList.remove(place)
        cityAdapter.notifyDataSetChanged()

        // Sauvegarde la liste mise à jour dans ScharedPreferences
        sharedPreferences.saveCityList(cityList)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.app_bar_search -> {
            //onSearchCalled()
            val fields = listOf(Place.Field.ID, Place.Field.NAME)
            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
            startAutocomplete.launch(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun onSearchCalled(){
        startAutocomplete = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK){
                val intent = result.data
                if (intent != null){
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    addCity(place)

                    //Log.i(TAG, "Place: ${place.name}, ${place.id}")
                    //Toast.makeText(this, "ID: " + place.id + "Adresse: " + place.address + "Nome: " + place.name + "Latitude/Longitude: " + place.latLng, Toast.LENGTH_LONG).show()
                }
            }else if (result.resultCode == Activity.RESULT_CANCELED){
                Log.i(TAG, "L'utilisateur a annulé l'autocomplétion")
                Toast.makeText(this, getString(R.string.result_cancel), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addCity(place: Place) {
        cityList.add(place)
        cityAdapter.notifyItemInserted(cityList.size - 1)

        sharedPreferences.saveCityList(cityList)
    }

    private fun startPlaceAutocomplete() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this)
        startAutocomplete.launch(intent)
    }


    override fun onResume() {
        super.onResume()
        val toolbarTitle = binding.toolbarTitle
        toolbarTitle.text = getString(R.string.favorite_place)

    }

}