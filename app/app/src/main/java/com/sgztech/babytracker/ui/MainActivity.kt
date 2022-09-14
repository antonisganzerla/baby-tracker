package com.sgztech.babytracker.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.sgztech.babytracker.R
import com.sgztech.babytracker.firebaseInstance
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val drawerLayout: DrawerLayout by lazy { findViewById(R.id.drawerLayout) }
    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val navView: NavigationView by lazy { findViewById(R.id.navView) }
    private val tvDate: TextView by lazy { findViewById(R.id.tvDate) }
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        setupDrawer()
    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        setupDrawerItemClickListener()
        setupHeaderDrawer()
        setupDatePicker()
    }

    private fun setupDrawerItemClickListener() {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_item_tools -> {
                    // TODO
                }
                R.id.nav_item_logout -> {
                    signOutFirebase()
                    openLoginActivity()
                }
                R.id.nav_item_about -> {
                    // TODO
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
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

    private fun setupHeaderDrawer() {
        val headerView = navView.getHeaderView(0)
        headerView?.let {
            val tvHeaderName = it.findViewById<TextView>(R.id.nav_header_name)
            val tvHeaderEmail = it.findViewById<TextView>(R.id.nav_header_email)
            val navHeaderImageView = it.findViewById<ImageView>(R.id.nav_header_imageView)
            firebaseInstance().currentUser?.let { user ->
                tvHeaderName.text =  user.displayName
                tvHeaderEmail.text = user.email
                Picasso.get().load(user.photoUrl).into(navHeaderImageView)
            }
        }
    }

    private fun setupDatePicker() {
        val onDateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            updateDate()
        }

        tvDate.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(this, onDateSetListener, year, month, day).show()
        }

        updateDate()
    }

    private fun updateDate() {
        val sdf = SimpleDateFormat("EEE, d MMM yyyy", Locale("pt", "BR"))
        tvDate.text = sdf.format(calendar.time)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }
}