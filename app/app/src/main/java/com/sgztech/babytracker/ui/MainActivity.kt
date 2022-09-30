package com.sgztech.babytracker.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dargoz.extendedbottomnavigationview.BottomNavigationBar
import com.dargoz.extendedbottomnavigationview.menu.SubMenuOrientation
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.navigation.NavigationView
import com.natura.android.button.TextButton
import com.sgztech.babytracker.R
import com.sgztech.babytracker.extension.*
import com.sgztech.babytracker.firebaseInstance
import com.sgztech.babytracker.model.Register
import com.sgztech.babytracker.ui.custom.*
import com.squareup.picasso.Picasso
import java.time.LocalDate
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val drawerLayout: DrawerLayout by lazy { findViewById(R.id.drawerLayout) }
    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val ivToolbar: ImageView by lazy { findViewById(R.id.ivToolbar) }
    private val cardIvBaby: MaterialCardView by lazy { findViewById(R.id.cardIvBaby) }
    private val navView: NavigationView by lazy { findViewById(R.id.navView) }
    private val tvDate: TextView by lazy { findViewById(R.id.tvDate) }
    private val buttonLeft: TextButton by lazy { findViewById(R.id.buttonLeft) }
    private val buttonRight: TextButton by lazy { findViewById(R.id.buttonRight) }
    private val btnRetry: MaterialButton by lazy { findViewById(R.id.btnRetry) }
    private val recyclerViewRegisters: RecyclerView by lazy { findViewById(R.id.recyclerViewRegisters) }
    private val panelEmptyMessage: LinearLayout by lazy { findViewById(R.id.panelEmptyMessage) }
    private val panelRetryMessage: LinearLayout by lazy { findViewById(R.id.panelRetryMessage) }
    private val bottomNavigationBar: BottomNavigationBar by lazy { findViewById(R.id.bottomNavigationView) }
    private val pbMain: ProgressBar by lazy { findViewById(R.id.pbMain) }
    private val viewModel: MainViewModel by viewModel()
    private var subMenuVisibility = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        setupDrawer()
        setupArrowButtons()
        setupRecyclerView(emptyList())
        setupBottomNavigationView()
        setupRetryButton()
        inscribeObservers()
    }

    private fun setupToolbar() {
        ivToolbar.setOnClickListener {
            openBabyActivity()
        }
        cardIvBaby.visibility = View.VISIBLE
        setSupportActionBar(toolbar)
    }

    private fun openBabyActivity() {
        val intent = Intent(this, BabyActivity::class.java)
        intent.putExtra(EDIT_KEY, true)
        startActivity(intent)
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
                R.id.nav_item_chart -> {
                    // TODO
                }
                R.id.nav_item_logout -> {
                    viewModel.logout()
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
            val user = viewModel.getUser()
            tvHeaderName.text = user.name
            tvHeaderEmail.text = user.email
            if (user.photoUri.isNullOrEmpty().not())
                Picasso.get().load(user.photoUri).into(navHeaderImageView)
        }
    }

    private fun setupDatePicker() {
        val onDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            viewModel.updateDate(LocalDate.of(year, month + 1, day))
        }

        tvDate.setOnClickListener {
            val date = viewModel.currentDate()
            val year = date.year
            val month = date.month.value - 1
            val day = date.dayOfMonth
            DatePickerDialog(this, onDateSetListener, year, month, day).show()
        }
    }

    private fun updateTvDate(date: LocalDate) {
        tvDate.text = viewModel.formatDate(date)
    }

    private fun setupArrowButtons() {
        buttonLeft.setOnClickListener { viewModel.minusDay() }
        buttonRight.setOnClickListener { viewModel.plusDay() }
    }

    private fun setupRecyclerView(registers: List<Register>) {
        if (registers.isEmpty()) {
            recyclerViewRegisters.gone()
            panelRetryMessage.gone()
            panelEmptyMessage.visible()
        } else {
            recyclerViewRegisters.visible()
            panelRetryMessage.gone()
            panelEmptyMessage.gone()
            recyclerViewRegisters.apply {
                adapter = RegisterAdapter(registers = registers) { selectedRegister ->
                    viewModel.deleteRegister(selectedRegister)
                }
                layoutManager = LinearLayoutManager(this@MainActivity)
                setHasFixedSize(true)
            }
        }
    }

    private fun setupBottomNavigationView() {
        bottomNavigationBar.addSubMenu(R.menu.sub_menu_bottom_list, 4, SubMenuOrientation.VERTICAL)
        bottomNavigationBar.showSubMenu(4, subMenuVisibility)
        bottomNavigationBar.setMenuOnClickListener { _, position: Int ->
            when (position) {
                0 -> {
                    FeedingModalBottomSheet(
                        date = viewModel.currentDate(),
                        actionButtonClick = { register, handleResult -> viewModel.addRegister(register, handleResult) },
                        successCallback = { saveRegisterSuccess() },
                    ).show(supportFragmentManager, FeedingModalBottomSheet.TAG)
                }
                1 -> {
                    DiaperModalBottomSheet(
                        date = viewModel.currentDate(),
                        actionButtonClick = { register, handleResult -> viewModel.addRegister(register, handleResult) },
                        successCallback = { saveRegisterSuccess() },
                    ).show(supportFragmentManager, DiaperModalBottomSheet.TAG)
                }
                2 -> {
                    SleepModalBottomSheet(
                        date = viewModel.currentDate(),
                        actionButtonClick = { register, handleResult -> viewModel.addRegister(register, handleResult) },
                        successCallback = { saveRegisterSuccess() },
                    ).show(supportFragmentManager, SleepModalBottomSheet.TAG)
                }
                3 -> {
                    BatheModalBottomSheet(
                        date = viewModel.currentDate(),
                        actionButtonClick = { register, handleResult -> viewModel.addRegister(register, handleResult) },
                        successCallback = { saveRegisterSuccess() },
                    ).show(supportFragmentManager, BatheModalBottomSheet.TAG)
                }
                4 -> {
                    subMenuVisibility = !subMenuVisibility
                    bottomNavigationBar.showSubMenu(position, subMenuVisibility)
                }
            }
        }
        bottomNavigationBar.setSubMenuOnClickListener { _, position ->
            when (position) {
                0 -> {
                    ColicModalBottomSheet(
                        date = viewModel.currentDate(),
                        actionButtonClick = { register, handleResult -> viewModel.addRegister(register, handleResult) },
                        successCallback = { saveRegisterSuccess() },
                    ).show(supportFragmentManager, ColicModalBottomSheet.TAG)
                }
                1 -> {
                    WeightModalBottomSheet(
                        date = viewModel.currentDate(),
                        actionButtonClick = { register, handleResult -> viewModel.addRegister(register, handleResult) },
                        successCallback = { saveRegisterSuccess() },
                    ).show(supportFragmentManager, WeightModalBottomSheet.TAG)
                }
                2 -> {
                    HeightModalBottomSheet(
                        date = viewModel.currentDate(),
                        actionButtonClick = { register, handleResult -> viewModel.addRegister(register, handleResult) },
                        successCallback = { saveRegisterSuccess() },
                    ).show(supportFragmentManager, HeightModalBottomSheet.TAG)
                }
                3 -> {
                    MedicalAppointmentModalBottomSheet(
                        date = viewModel.currentDate(),
                        actionButtonClick = { register, handleResult -> viewModel.addRegister(register, handleResult) },
                        successCallback = { saveRegisterSuccess() },
                    ).show(supportFragmentManager, MedicalAppointmentModalBottomSheet.TAG)
                }
            }
        }
    }

    private fun saveRegisterSuccess() {
        bottomNavigationBar.showSnackbar(R.string.msg_save_successful)
        viewModel.loadRegisters()
    }

    private fun setupRetryButton() {
        btnRetry.setOnClickListener {
            viewModel.loadRegisters()
        }
    }

    private fun inscribeObservers() {
        viewModel.loadAction.observe(this) { action ->
            when (action) {
                is RequestAction.GenericFailure -> {
                    pbMain.hide()
                    recyclerViewRegisters.gone()
                    panelEmptyMessage.gone()
                    panelRetryMessage.visible()
                    bottomNavigationBar.showSnackbar(action.errorRes)
                }
                RequestAction.Loading -> {
                    pbMain.show()
                    panelRetryMessage.gone()
                }
                is RequestAction.Success<*> -> {
                    pbMain.hide()
                    setupRecyclerView(action.value as List<Register>)
                }
                is RequestAction.ValidationFailure -> {
                    pbMain.hide()
                    recyclerViewRegisters.gone()
                    panelEmptyMessage.gone()
                    panelRetryMessage.visible()
                    bottomNavigationBar.showSnackbar(action.errors.joinToString())
                }
            }
        }

        viewModel.date.observe(this) { date ->
            updateTvDate(date)
            viewModel.loadRegisters()
            val period = viewModel.getPeriodBetween()
            toolbar.subtitle = if (period.isNegative)
                getString(R.string.before_born)
            else
                getString(R.string.date_between, period.months, period.days)
        }

        viewModel.baby.observe(this) { baby ->
            toolbar.title = baby.name
            if (baby.photoUri.isNotEmpty()) {
                Picasso.get().load(baby.photoUri).into(ivToolbar)
            }
        }
    }

    private fun ProgressBar.show() {
        visible()
        disableUserInteraction()
    }

    private fun ProgressBar.hide() {
        gone()
        enableUserInteraction()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}