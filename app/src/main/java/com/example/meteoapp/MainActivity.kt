package com.example.meteoapp

<<<<<<< HEAD
import android.content.Intent
=======
>>>>>>> 66331d2c6b518470ccecdcd76e1d77bda278a5ee
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
<<<<<<< HEAD
import com.example.meteoapp.auth.MainLoginActivity
import com.example.meteoapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var firebaseAuth: FirebaseAuth
=======
import com.example.meteoapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding : ActivityMainBinding
>>>>>>> 66331d2c6b518470ccecdcd76e1d77bda278a5ee

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

<<<<<<< HEAD
=======
        @Suppress("UNUSED_VARIABLE")
>>>>>>> 66331d2c6b518470ccecdcd76e1d77bda278a5ee
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        //put all the drawerlayout into the appbarconfiguration
        //with layout_gravity we don't have the superposition of the view
        drawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        // configurate the toolbar with the navView to be used by the navController
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

<<<<<<< HEAD
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser : FirebaseUser? = firebaseAuth.currentUser

        if(currentUser == null){
            val intent = Intent(this, MainLoginActivity::class.java)
            startActivity(intent)// findNavController().navigate(R.id.action_fragment_to_navigation_login)
            finish()
        }


=======
>>>>>>> 66331d2c6b518470ccecdcd76e1d77bda278a5ee
    }

    //button di ritorno nella toolbar
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}