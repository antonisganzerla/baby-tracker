package com.sgztech.babytracker.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Baby
import com.squareup.picasso.Picasso
import java.time.LocalDate

class BabyActivity : AppCompatActivity() {

    private val etName: TextInputEditText by lazy { findViewById(R.id.etName) }
    private val btnSaveBaby: MaterialButton by lazy { findViewById(R.id.btnSaveBaby) }
    private val textInputLayoutBirthday: TextInputLayout by lazy { findViewById(R.id.textInputLayoutBirthday) }
    private val autoCompleteBirthday: MaterialAutoCompleteTextView by lazy { findViewById(R.id.autoCompleteBirthday) }
    private val autoCompleteSex: MaterialAutoCompleteTextView by lazy { findViewById(R.id.autoCompleteSex) }
    private val ivBaby: ImageView by lazy { findViewById(R.id.ivBaby) }
    private val tvBabyPhoto: MaterialTextView by lazy { findViewById(R.id.tvBabyPhoto) }
    private val viewModel: BabyViewModel by viewModels()
    private var photoUri: String = ""

    private val galleryActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val imageUri = result.data?.data
        imageUri?.let {
            Picasso.get().load(it).into(ivBaby)
            tvBabyPhoto.text = getString(R.string.edit_baby_photo)
            photoUri = it.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby)
        setupToolbar()
        setupBtnSave()
        setupDatePicker()
        setupAutoCompleteSex()
        setupImageViewClick()
        subscribeObservers()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.toolbar_title_baby)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setupBtnSave() {
        btnSaveBaby.setOnClickListener {
            val baby = Baby(
                name = etName.text.toString(),
                birthday = viewModel.currentDate(),
                sex = autoCompleteSex.text.toString(),
                photoUri = photoUri,
            )
            viewModel.saveBaby(baby, this)
            openMainActivity()
        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupDatePicker() {
        textInputLayoutBirthday.setEndIconOnClickListener {
            popDatePicker()
        }
        autoCompleteBirthday.apply {
            dropDownHeight = 0
            setOnClickListener {
                popDatePicker()
            }
        }
    }

    private fun popDatePicker() {
        val onDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            viewModel.updateDate(LocalDate.of(year, month + 1, day))
        }
        val date = viewModel.currentDate()
        val year = date.year
        val month = date.month.value - 1
        val day = date.dayOfMonth
        DatePickerDialog(this, onDateSetListener, year, month, day).show()
    }

    private fun setupAutoCompleteSex() {
        val adapter = ArrayAdapter(
            this,
            R.layout.dropdown_item,
            resources.getStringArray(R.array.sex_options)
        )
        autoCompleteSex.setAdapter(adapter)
    }

    private fun setupImageViewClick() {
        ivBaby.setOnClickListener { openGallery() }
        tvBabyPhoto.setOnClickListener { openGallery() }
    }

    private fun openGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        galleryActivityResult.launch(photoPickerIntent)
    }

    private fun subscribeObservers() {
        viewModel.date.observe(this) { date ->
            autoCompleteBirthday.setText(viewModel.formatDate(date))
        }
    }
}