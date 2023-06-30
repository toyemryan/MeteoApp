package com.example.meteoapp.mainMeteo

//import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
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

        binding.Goto.setOnClickListener{  view : View ->
            view.findNavController().navigate(R.id.action_mainMeteoFragment_to_loginFragment) }

        val toolbar = (activity as AppCompatActivity).findViewById<Toolbar>(R.id.toolbar)

        toolbar.title = "Ancona"


        return binding.root
    }


}