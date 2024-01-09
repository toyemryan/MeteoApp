/*
 * Copyright (c) 09/01/2024 Toyem Tezem Ryan Parfait & Djouaka Kelefack Lionel all rights reserved
 */

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
import com.example.meteoapp.Coordinator
import com.example.meteoapp.R
import com.example.meteoapp.databinding.FragmentLoginBinding
import com.example.meteoapp.repository.Repository
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        val ss = SpannableString("Non sei registrato ? Registrati")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                view?.findNavController()?.navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
        ss.setSpan(clickableSpan, 21, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val textView = binding.textView
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance()
        binding.btnLogin.setOnClickListener { registrati() }
        firebaseAuth = FirebaseAuth.getInstance()
        return binding.root
    }


    private fun registrati() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()


        if (email.trim().isEmpty() || !email.matches(EMAIL_PATTERN.toRegex())) {
            binding.email.error = getString(R.string.invalid_email_login)
            return
        } else {
            binding.email.error = null
        }

        if (password.trim().isEmpty()) {
            binding.password.error = getString(R.string.password_required)
            return
        } else if (password.trim().length < 8) {
            binding.password.error = getString(R.string.password_length_error)
            return
        } else {
            binding.password.error = null
        }

        if (email.trim().isEmpty() && password.trim().isEmpty()) {
            binding.email.error = getString(R.string.email_required)
            binding.password.error = getString(R.string.password_required)
            return
        } else {
            binding.email.error = null
            binding.password.error = null
        }

        if (!Repository().isNetworkAvailable(requireContext())) {
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

}
