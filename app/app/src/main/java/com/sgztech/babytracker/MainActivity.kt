package com.sgztech.babytracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogoff = findViewById<Button>(R.id.btn_logoff)
        btnLogoff.setOnClickListener {
            signOutGoogle()
        }
    }

    private fun signOutGoogle() {
        googleSignInClient(this).signOut()
            .addOnCompleteListener(this) {
                signOutFirebase()
                openLoginActivity()
            }
    }

    private fun signOutFirebase() {
        firebaseInstance().signOut()
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}