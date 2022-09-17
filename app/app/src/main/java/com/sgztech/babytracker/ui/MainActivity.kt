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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.natura.android.button.TextButton
import com.sgztech.babytracker.R
import com.sgztech.babytracker.firebaseInstance
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.ui.custom.DiaperModalBottomSheet
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    private val drawerLayout: DrawerLayout by lazy { findViewById(R.id.drawerLayout) }
    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val navView: NavigationView by lazy { findViewById(R.id.navView) }
    private val tvDate: TextView by lazy { findViewById(R.id.tvDate) }
    private val buttonLeft: TextButton by lazy { findViewById(R.id.buttonLeft) }
    private val buttonRight: TextButton by lazy { findViewById(R.id.buttonRight) }
    private val recyclerViewRegisters: RecyclerView by lazy { findViewById(R.id.recyclerViewRegisters) }
    private val bottomNavigationView: BottomNavigationView by lazy { findViewById(R.id.bottomNavigationView) }
    private var datetime: LocalDate = LocalDate.now()
    private val registers: MutableList<Register> = fakeData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        setupDrawer()
        setupArrowButtons()
        setupRecyclerView(registers)
        setupBottomNavigationView()
    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
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
                tvHeaderName.text = user.displayName
                tvHeaderEmail.text = user.email
                Picasso.get().load(user.photoUrl).into(navHeaderImageView)
            }
        }
    }

    private fun setupDatePicker() {
        val onDateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            datetime = datetime.withYear(year).withMonth(month + 1).withDayOfMonth(day)
            updateDate()
        }

        tvDate.setOnClickListener {
            val year = datetime.year
            val month = datetime.month.value - 1
            val day = datetime.dayOfMonth
            DatePickerDialog(this, onDateSetListener, year, month, day).show()
        }

        updateDate()
    }

    private fun updateDate() {
        val formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale("pt", "BR"))
        tvDate.text = datetime.format(formatter)
    }

    private fun setupArrowButtons() {
        buttonLeft.setOnClickListener {
            datetime = datetime.minusDays(1)
            updateDate()
        }
        buttonRight.setOnClickListener {
            datetime = datetime.plusDays(1)
            updateDate()
        }
    }

    private fun setupRecyclerView(registers : List<Register>) {
        recyclerViewRegisters.apply {
            adapter = RegisterAdapter(
                registers = registers,
            )
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }

    private fun setupBottomNavigationView() {
        bottomNavigationView.menu.setGroupCheckable(0, false, true)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_feeding -> {
                    true
                }
                R.id.nav_diaper -> {
                    val modalBottomSheet = DiaperModalBottomSheet(
                        actionButtonClick = { register ->
                            registers.add(register)
                            setupRecyclerView(registers)
                        }
                    )
                    modalBottomSheet.show(supportFragmentManager, DiaperModalBottomSheet.TAG)
                    true
                }
                R.id.nav_bathe -> {
                    true
                }
                else -> false
            }
        }
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

    private fun fakeData() = mutableListOf(
        Register(
            icon = R.drawable.ic_bathtub_24,
            name = "Banho",
            description = "",
            time = LocalDateTime.now().plusHours(1),
        ),
        Register(
            icon = R.drawable.ic_food_bank_24,
            name = "Amamentação",
            description = "Esquerda 15:10",
            time = LocalDateTime.now().plusHours(2),
        ),
    )
}