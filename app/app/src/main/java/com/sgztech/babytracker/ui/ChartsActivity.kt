package com.sgztech.babytracker.ui

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sgztech.babytracker.R
import com.sgztech.babytracker.ui.charts.DiaperFragment
import com.sgztech.babytracker.ui.charts.FeedingFragment
import com.sgztech.babytracker.ui.charts.GrowingFragment
import com.sgztech.babytracker.ui.charts.SleepFragment

class ChartsActivity : BaseActivity() {

    private val bottomNavigationView: BottomNavigationView by lazy { findViewById(R.id.bottomNavigationView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setupToolbar(toolbar, R.string.toolbar_title_charts)

        replaceFragment(FeedingFragment())

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_feeding -> replaceFragment(FeedingFragment())
                R.id.nav_growing -> replaceFragment(GrowingFragment())
                R.id.nav_diaper -> replaceFragment(DiaperFragment())
                R.id.nav_sleep -> replaceFragment(SleepFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentContainer, fragment)
        transaction.commit()
    }
}
