package com.sgztech.babytracker.ui

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.sgztech.babytracker.R

class ResetPasswordActivity  : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setupToolbar(toolbar, R.string.toolbar_title_register_new_password)
    }


}