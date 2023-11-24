package com.example.meteoapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.meteoapp.R
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        supportActionBar?.title = getString(R.string.profile)

        // Récupérer l'email de l'utilisateur depuis l'Intent
        val userEmail = intent.getStringExtra("userEmail")

        // Utiliser l'email récupéré, par exemple l'afficher dans un TextView
        val userEmailTextView: TextView = findViewById(R.id.user_email)
        userEmailTextView.text = userEmail

        // Récupérer le bouton de suppression
        val deleteButton: Button = findViewById(R.id.button_cancel)

        // Associer un écouteur de clic au bouton de suppression
        deleteButton.setOnClickListener {
            deleteProfile()
        }

    }


    private fun deleteProfile() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage(R.string.confirm)
                .setCancelable(false)
                .setPositiveButton(R.string.sip) { _, _ ->
                    user.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Profilo cancellato con successo
                                FirebaseAuth.getInstance().signOut()
                                startActivity(Intent(this, MainLoginActivity::class.java))
                                finish()
                                Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                .setNegativeButton(R.string.nop) { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = dialogBuilder.create()
            alert.setTitle(R.string.cancell)
            alert.show()
        }
    }


}


