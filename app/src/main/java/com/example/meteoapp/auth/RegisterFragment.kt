package com.example.meteoapp.auth

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

  //  private lateinit var binding: FragmentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(
            inflater, R.layout.fragment_register, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        /*binding.textView.setOnClickListener{
            view?.findNavController()?.navigate(R.id.action_registerFragment_to_loginFragment) } */


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


        binding.btnRegister.setOnClickListener{
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confermaPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword){
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(requireActivity(), "Registration successfully completed", Toast.LENGTH_SHORT).show()
                            view?.findNavController()?.navigate(R.id.action_registerFragment_to_loginFragment)
                          //  view?.findNavController()?.navigate(R.id.action_registerFragment_to_navigation)
                        }else{
                            Toast.makeText(requireActivity(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(requireActivity(), "Password non Corrisponde", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireActivity(), "Devi compilare tutti i campi !!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }


}