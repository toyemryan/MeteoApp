package com.example.meteoapp.mainMeteo

<<<<<<< HEAD
=======
//import androidx.lifecycle.ViewModelProvider
>>>>>>> 66331d2c6b518470ccecdcd76e1d77bda278a5ee
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
<<<<<<< HEAD
=======
import androidx.navigation.findNavController
>>>>>>> 66331d2c6b518470ccecdcd76e1d77bda278a5ee
import com.example.meteoapp.R
import com.example.meteoapp.databinding.FragmentMainMeteoBinding
import kotlin.math.abs
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.appbar.AppBarLayout


class MainMeteoFragment : Fragment() {


  //  private lateinit var viewModel: MainMeteoViewModel

<<<<<<< HEAD
    //private lateinit var firebaseAuth: FirebaseAuth

=======
>>>>>>> 66331d2c6b518470ccecdcd76e1d77bda278a5ee
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil.inflate<FragmentMainMeteoBinding>(inflater,
            R.layout.fragment_main_meteo,container,false)

        hide()

<<<<<<< HEAD
=======
        binding.Goto.setOnClickListener{  view : View ->
            view.findNavController().navigate(R.id.action_mainMeteoFragment_to_loginFragment) }

>>>>>>> 66331d2c6b518470ccecdcd76e1d77bda278a5ee
        val toolbar = (activity as AppCompatActivity).findViewById<Toolbar>(R.id.toolbar)

        toolbar.title = "Ancona"

<<<<<<< HEAD
       /* firebaseAuth = FirebaseAuth.getInstance()

        val currentUser : FirebaseUser? = firebaseAuth.currentUser

        if(currentUser == null){

            findNavController().navigate(R.id.action_fragment_to_navigation_login)
        } */

=======
>>>>>>> 66331d2c6b518470ccecdcd76e1d77bda278a5ee
        return binding.root
    }


    //function to hide the status bar when the user scrolling
    private fun hide (){
        val window = (activity as AppCompatActivity).window
        val appBarLayout = (activity as AppCompatActivity).findViewById<AppBarLayout>(R.id.appbarlayout)
        appBarLayout.addOnOffsetChangedListener { AppBarLayout, verticalOffset ->

            if (abs(verticalOffset) == AppBarLayout.totalScrollRange) {

                // Collapsed
                /*appBarLayout.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_FULLSCREEN) //this one changed*/
                WindowCompat.setDecorFitsSystemWindows(window, false)
                WindowInsetsControllerCompat(window, AppBarLayout).let { controller ->
                    controller.hide(WindowInsetsCompat.Type.systemBars())
                    controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else if (verticalOffset == 0) {

                // Fully Expanded - show the status bar
                /*appBarLayout.systemUiVisibility = (View.SYSTEM_UI_FLAG_VISIBLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        /*or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN*/) */
                WindowCompat.setDecorFitsSystemWindows(window, true)
                WindowInsetsControllerCompat(window, AppBarLayout).show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

}