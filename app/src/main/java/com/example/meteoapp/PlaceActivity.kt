package com.example.meteoapp

import android.app.Activity
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
import com.example.meteoapp.databinding.ActivityPlaceBinding
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class PlaceActivity : AppCompatActivity() {

      private lateinit var binding: ActivityPlaceBinding
      private lateinit var startAutocomplete: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_place)

        binding = ActivityPlaceBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar2)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        val apiKey = getString(R.string.api_key)

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(this, apiKey)
        }
        // Create a new Places client instance.
        val placesClient = Places.createClient(this)

        onSearchCalled()

        setContentView(binding.root)
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

        private fun onSearchCalled() {

            startAutocomplete =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val intent = result.data
                        if (intent != null) {
                            val place = Autocomplete.getPlaceFromIntent(intent)
                            Log.i(TAG, "Place: ${place.name}, ${place.id}")
                            Toast.makeText(this, "ID: " + place.id + "address:" + place.address + "Name:" + place.name + " latlong: " + place.latLng, Toast.LENGTH_LONG).show()
                        }
                    } else if (result.resultCode == Activity.RESULT_CANCELED) {
                        // The user canceled the operation.
                        Log.i(TAG, "User canceled autocomplete")
                        Toast.makeText(this, getString(R.string.result_cancel), Toast.LENGTH_LONG).show()
                    }
                }

    }

    override fun onResume() {
        super.onResume()
        val toolbarTitle = binding.toolbarTitle
        toolbarTitle.text = getString(R.string.favorite_place)

    }

}