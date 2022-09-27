package com.sgztech.babytracker.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.sgztech.babytracker.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        viewModel.navigateState.observe(this) { navigateState ->
            when (navigateState) {
                SplashNavigateState.BabyFormScreen -> openBabyActivity()
                SplashNavigateState.LoginScreen -> openLoginActivity()
                SplashNavigateState.MainScreen -> openMainActivity()
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.navigateToNextScreen()
        }, 2000)
    }

    private fun openBabyActivity() {
        val intent = Intent(this, BabyActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}