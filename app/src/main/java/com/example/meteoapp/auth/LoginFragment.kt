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

     private fun registrati() {

             val email = binding.email.text.toString()
             val password = binding.password.text.toString()

             if (email.isNotEmpty() && password.isNotEmpty()){
                 firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                     if (it.isSuccessful){
                       /*  val intent = Intent(this, MainActivity::class.java)
                         startActivity(intent) */
                        // view?.findNavController()?.navigate(R.id.action_loginFragment2_to_navigation)
                         val myActivity = activity
                         if(myActivity is Coordinator){
                         myActivity.gotomainmeteo() // la chiamata al metodo onBookChanged nel activity, che è stato anche creato nel interfaccia Coordinator
                     }
                     }else{
                         Toast.makeText(requireActivity(),"Lutente non esiste", Toast.LENGTH_SHORT).show()
                     }
                 }
             }else{
                 Toast.makeText( requireActivity(), "Devi compilare tutti i campi !!", Toast.LENGTH_SHORT).show()
             }

     }

}