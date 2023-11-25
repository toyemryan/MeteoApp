package com.example.meteoapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.meteoapp.auth.MainLoginActivity
import com.example.meteoapp.auth.ProfileActivity
import com.example.meteoapp.databinding.ActivityMainBinding
import com.example.meteoapp.mainMeteo.MainMeteoFragment
import com.example.meteoapp.repository.Repository
import com.example.meteoapp.setting.SettingActivity
import com.example.meteoapp.setting.languageChange.DefaultLocaleHelper
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var firebaseAuth: FirebaseAuth
    private var currentUser : FirebaseUser? = null


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController


        //put all the drawerlayout into the appbarconfiguration
        //with layout_gravity we don't have the superposition of the view
        drawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        // configurate the toolbar with the navView to be used by the navController
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        NavigationUI.setupWithNavController(binding.navView, navController)

        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser

        if(currentUser == null){
            val intent = Intent(this, MainLoginActivity::class.java)
            startActivity(intent)// findNavController().navigate(R.id.action_fragment_to_navigation_login)
            finish()
        }

        // cet appel prepare les menu du NavView à d'éventuel changement en occurence onNavigationItemSelected
        binding.navView.setNavigationItemSelectedListener(this)


    }

    //button di ritorno nella toolbar
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    // cette fonction permet de linker les menu du navView afin d'implementer un clickListener
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        when (item.itemId) {

            R.id.actualLocation -> {
                if (Repository().isNetworkAvailable(this) && Repository.citynow != null){
                    Repository.cityname = Repository.citynow
                    /*val frag = MainMeteoFragment()*/
                    drawerLayout.closeDrawer(GravityCompat.START)

                }
                else  {
                    Toast.makeText(this, R.string.no_network_connection, Toast.LENGTH_SHORT).show()
                }
            }
            R.id.profile -> {
                val userEmail = currentUser?.email // Obtenez l'email de l'utilisateur connecté
                val intent = Intent(this, ProfileActivity::class.java).apply {
                    putExtra("userEmail", userEmail)
                }
                startActivity(intent)
            }
            R.id.logout -> {
                showLogoutConfirmDialog()
            }
            R.id.setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.placeFragment -> {
               val intent = Intent(this, PlaceActivity::class.java)
                startActivity(intent)
            }
            R.id.aboutFragment -> {
                navController.navigate(R.id.action_mainMeteoFragment_to_aboutFragment)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(DefaultLocaleHelper.getInstance(newBase!!).onAttach())
    }

    private fun showLogoutConfirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.logout)
        builder.setMessage(R.string.exit_1)
        builder.setPositiveButton(R.string.yes) { dialogInterface: DialogInterface, id: Int ->
            firebaseAuth.signOut()
            currentUser = null
            val intent = Intent(this, MainLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton(R.string.no){ dialogInterface: DialogInterface, id: Int ->
            dialogInterface.dismiss()
        }
       builder.setNeutralButton(R.string.chiudi){ dialogInterface: DialogInterface, id: Int ->
            dialogInterface.cancel()
        }
        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()
    }

}