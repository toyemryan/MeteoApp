package com.example.meteoapp.auth

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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.meteoapp.R
import com.example.meteoapp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(
            inflater, R.layout.fragment_register, container, false
        )

        firebaseAuth = FirebaseAuth.getInstance()

        val ss = SpannableString("Hai un account ? Accedi")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                view?.findNavController()?.navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }
        ss.setSpan(clickableSpan, 17, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val textView = binding.textView
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance()

        binding.btnRegister.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confermaPassword.text.toString()

            when {
                email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                    showToast(R.string.tutti_campi_richiesti)
                }
                password != confirmPassword -> {
                    showToast(R.string.password_non_corrisponde)
                }
                !isInternetAvailable(requireContext()) -> {
                    showToast(R.string.no_network_connection)
                }
                else -> {
                    createUserWithEmailAndPassword(email, password)
                }
            }
        }

        return binding.root
    }

    private fun createUserWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast(R.string.succes)
                view?.findNavController()?.navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                showToast(task.exception.toString())
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(messageResId: Int) {
        showToast(getString(messageResId))
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        } else {
            false
        }
    }
}
