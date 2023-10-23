package com.example.meteoapp.mainMeteo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.meteoapp.R
import com.example.meteoapp.databinding.FragmentMainMeteoBinding

class MainMeteoFragment : Fragment() {


  //  private lateinit var viewModel: MainMeteoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil.inflate<FragmentMainMeteoBinding>(inflater,
            R.layout.fragment_main_meteo,container,false)

       /* val toolbar = (activity as AppCompatActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Ancona" */

       /* val toolbarTitle = (activity as AppCompatActivity).findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Ancona" */

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val toolbarTitle = (activity as AppCompatActivity).findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = null
    }

}