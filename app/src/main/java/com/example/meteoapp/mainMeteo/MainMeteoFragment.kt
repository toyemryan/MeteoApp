package com.example.meteoapp.mainMeteo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.meteoapp.R
import com.example.meteoapp.databinding.FragmentMainMeteoBinding
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs


class MainMeteoFragment : Fragment() {


  //  private lateinit var viewModel: MainMeteoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil.inflate<FragmentMainMeteoBinding>(inflater,
            R.layout.fragment_main_meteo,container,false)
        hide()

       /* val toolbar = (activity as AppCompatActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Ancona" */

        val toolbarTitle = (activity as AppCompatActivity).findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Ancona"

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