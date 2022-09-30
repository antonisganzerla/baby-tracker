package com.sgztech.babytracker.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.sgztech.babytracker.R
import com.sgztech.babytracker.extension.disableUserInteraction
import com.sgztech.babytracker.extension.enableUserInteraction
import com.sgztech.babytracker.extension.gone
import com.sgztech.babytracker.extension.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val pbMain: ProgressBar by lazy { findViewById(R.id.pbSplash) }
    private val ivIcon: ImageView by lazy { findViewById(R.id.ivIcon) }
    private val panelRetryMessage: LinearLayout by lazy { findViewById(R.id.panelRetryMessage) }
    private val btnRetry: MaterialButton by lazy { findViewById(R.id.btnRetry) }
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
                SplashNavigateState.Error -> {
                    pbMain.hide()
                    ivIcon.gone()
                    panelRetryMessage.visible()
                }
            }
        }

        btnRetry.setOnClickListener {
            pbMain.show()
            ivIcon.visible()
            panelRetryMessage.gone()
            viewModel.navigateToNextScreen()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.navigateToNextScreen()
        }, 2000)
    }

    private fun ProgressBar.show() {
        visible()
        disableUserInteraction()
    }

    private fun ProgressBar.hide() {
        gone()
        enableUserInteraction()
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