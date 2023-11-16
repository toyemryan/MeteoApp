package com.example.meteoapp.auth

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.meteoapp.Coordinator
import com.example.meteoapp.R
import com.example.meteoapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

     /*   binding.textView.setOnClickListener{  view : View ->
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment) } */

        // metodo spannable per rendere clickabile solo la parola registrati
        val ss = SpannableString("Non sei registrato ? Registrati")
        //val ss = SpannableString(R.string.action_register.toString())
        val clickableSpan: ClickableSpan = object : ClickableSpan() {

            override fun onClick(textView: View) {
                view?.findNavController()?.navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
        ss.setSpan(clickableSpan, 21, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val textView = binding.textView
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance() // metodo che formalizza il link della parola


        binding.btnLogin.setOnClickListener{registrati()}

        firebaseAuth = FirebaseAuth.getInstance()

        return binding.root
    }

     @SuppressLint("SuspiciousIndentation")
     private fun registrati() {
         val email = binding.email.text.toString()
         val password = binding.password.text.toString()

         if (email.trim().isEmpty() || password.trim().isEmpty()) {
             Toast.makeText(requireActivity(), R.string.tutti_campi_richiesti, Toast.LENGTH_SHORT).show()
             return
         }

         if (!isInternetAvailable(requireContext())) {
             Toast.makeText(requireActivity(), R.string.no_network_connection, Toast.LENGTH_SHORT).show()
             return
         }

         firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
             if (task.isSuccessful) {
                 val myActivity = activity
                 if (myActivity is Coordinator) {
                     myActivity.gotomainmeteo()
                 }
             } else {
                 Toast.makeText(requireActivity(), R.string.login_failed, Toast.LENGTH_SHORT).show()
             }
         }
     }
     private fun isInternetAvailable(context: Context): Boolean{
         val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         val network = connectivityManager.activeNetwork ?: return false
         val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
         return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
     }
}