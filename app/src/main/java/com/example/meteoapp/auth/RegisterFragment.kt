package com.example.meteoapp.auth

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.meteoapp.R
import com.example.meteoapp.databinding.FragmentRegisterBinding
import com.example.meteoapp.repository.Repository
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

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
                email.trim().isEmpty()|| !email.matches(EMAIL_PATTERN.toRegex()) ->{
                    binding.email.error = getString(R.string.invalid_email_login)
                }

                email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                    binding.password.error = getString(R.string.tutti_campi_richiesti)
                }
                password != confirmPassword -> {
                    binding.password.error = getString(R.string.password_non_corrisponde)
                }
                password.trim().length < 8 -> {
                    binding.password.error = getString(R.string.password_length_error)
                }
                !Repository().isNetworkAvailable(requireContext()) -> {
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

}
