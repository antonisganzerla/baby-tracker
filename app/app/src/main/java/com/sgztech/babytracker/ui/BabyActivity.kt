package com.sgztech.babytracker.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.sgztech.babytracker.R
import java.io.InputStream
import java.time.LocalDate

class BabyActivity : AppCompatActivity() {

    private val btnSaveBaby: MaterialButton by lazy { findViewById(R.id.btnSaveBaby) }
    private val textInputLayoutBirthday: TextInputLayout by lazy { findViewById(R.id.textInputLayoutBirthday) }
    private val autoCompleteBirthday: MaterialAutoCompleteTextView by lazy { findViewById(R.id.autoCompleteBirthday) }
    private val autoCompleteSex: MaterialAutoCompleteTextView by lazy { findViewById(R.id.autoCompleteSex) }
    private val ivBaby: ImageView by lazy { findViewById(R.id.ivBaby) }
    private val tvBabyPhoto: MaterialTextView by lazy { findViewById(R.id.tvBabyPhoto) }
    private var date: LocalDate = LocalDate.now()
    private val formatter: DateTimeFormatter = DateTimeFormatter()

    private val galleryActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val imageUri = result.data?.data
        imageUri?.let {
            val imageStream: InputStream? = contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(imageStream)
            val bitmapReduced = Bitmap.createScaledBitmap(bitmap, 300, 300, true)
            ivBaby.setImageBitmap(bitmapReduced)
            ivBaby.scaleType = ImageView.ScaleType.FIT_XY
            tvBabyPhoto.text = getString(R.string.edit_baby_photo)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.toolbar_title_baby)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        btnSaveBaby.setOnClickListener {
            openMainActivity()
        }
        setupDatePicker()
        setupAutoCompleteSex()
        ivBaby.setOnClickListener { openGallery() }
        tvBabyPhoto.setOnClickListener { openGallery() }
    }

    private fun openGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        galleryActivityResult.launch(photoPickerIntent)
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
            date = LocalDate.of(year, month + 1, day)
            autoCompleteBirthday.setText(formatter.format(date, "dd/MM/yyyy"))
        }
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
}