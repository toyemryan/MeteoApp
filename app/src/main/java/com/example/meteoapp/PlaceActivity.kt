package com.example.meteoapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
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
        binding = ActivityPlaceBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar2)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val apiKey = getString(R.string.api_key)

        if (!Places.isInitialized()) {
            Places.initialize(this, apiKey)
        }

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

    private fun showConfirmationDialog(place: Place) {
        val builder = AlertDialog.Builder(this)
        val deleteMessage = getString(R.string.deletecity, place.name)
        builder.setTitle(R.string.deletecity1)
            .setMessage(deleteMessage)
            .setPositiveButton(R.string.yes){_, _ -> removeCity(place)}
            .setNegativeButton(R.string.no){dialog, _ -> dialog.dismiss()}.show()
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

    /** qua facciamo un intent alla pagina che fa la ricerca da l'API PLACE di google
     * @param item
     */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.app_bar_search -> {
            val fields = listOf(Place.Field.ID, Place.Field.NAME)
            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
            startAutocomplete.launch(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    /** funzione di call back che recupera i dati da ritornati dall'API
     */
    private fun onSearchCalled(){
        startAutocomplete = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK){
                val intent = result.data
                if (intent != null){
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    addCity(place)
                }
            }else if (result.resultCode == Activity.RESULT_CANCELED){
                //Log.i(TAG, R.string.cancel_1.toString())
                Toast.makeText(this, getString(R.string.result_cancel), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addCity(place: Place) {
        val isCityExist = cityList.any{it.id == place.id}
        if (!isCityExist){
            cityList.add(place)
            cityAdapter.notifyItemInserted(cityList.size - 1)
            sharedPreferences.saveCityList(cityList)
        }else{
            Toast.makeText(this, R.string.exist, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        val toolbarTitle = binding.toolbarTitle
        toolbarTitle.text = getString(R.string.favorite_place)

    }

    fun onCityLongClick(place: Place){
        showConfirmationDialog(place)
    }
    fun onCityClick(place: Place){
        Repository.cityname = place.name?.toString()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
