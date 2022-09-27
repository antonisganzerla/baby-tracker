package com.sgztech.babytracker.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Baby
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

const val EDIT_KEY = "edit"

class BabyActivity : AppCompatActivity() {

    private val textInputLayoutName: TextInputLayout by lazy { findViewById(R.id.textInputLayoutName) }
    private val etName: TextInputEditText by lazy { findViewById(R.id.etName) }
    private val btnSaveBaby: MaterialButton by lazy { findViewById(R.id.btnSaveBaby) }
    private val textInputLayoutBirthday: TextInputLayout by lazy { findViewById(R.id.textInputLayoutBirthday) }
    private val textInputLayoutSex: TextInputLayout by lazy { findViewById(R.id.textInputLayoutSex) }
    private val autoCompleteBirthday: MaterialAutoCompleteTextView by lazy { findViewById(R.id.autoCompleteBirthday) }
    private val autoCompleteSex: MaterialAutoCompleteTextView by lazy { findViewById(R.id.autoCompleteSex) }
    private val ivBaby: ImageView by lazy { findViewById(R.id.ivBaby) }
    private val tvBabyPhoto: MaterialTextView by lazy { findViewById(R.id.tvBabyPhoto) }
    private val viewModel: BabyViewModel by viewModel()
    private var photoUri: String = ""
    private var isEditingMode: Boolean = false

    private val galleryActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val imageUri = result.data?.data
        imageUri?.let {
            loadBabyImage(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby)

        isEditingMode = intent.getBooleanExtra(EDIT_KEY, false)

        setupToolbar()
        setupBtnSave()
        setupDatePicker()
        setupAutoCompleteSex()
        setupImageViewClick()
        subscribeObservers()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (isEditingMode) {
            supportActionBar?.title = getString(R.string.toolbar_title_edit_baby)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar?.title = getString(R.string.toolbar_title_add_baby)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun loadBabyInfo(baby: Baby) {
        etName.setText(baby.name)
        autoCompleteBirthday.setText(viewModel.formatDate(baby.birthday))
        autoCompleteSex.setText(baby.sex)
        if (baby.photoUri.isNotEmpty())
            loadBabyImage(baby.photoUri.toUri())
        viewModel.updateDate(baby.birthday)
    }

    private fun loadBabyImage(imageUri: Uri) {
        Picasso.get().load(imageUri).into(ivBaby)
        tvBabyPhoto.text = getString(R.string.edit_baby_photo)
        photoUri = imageUri.toString()
    }

    private fun setupBtnSave() {
        btnSaveBaby.setOnClickListener {
            viewModel.validate(
                name = etName.text.toString(),
                sex = autoCompleteSex.text.toString(),
            )
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
        viewModel.formState.observe(this) { formState ->
            when (formState) {
                is BabyFormState.InvalidName -> {
                    textInputLayoutName.isErrorEnabled = true
                    textInputLayoutName.error = getString(formState.errorRes)
                    textInputLayoutSex.isErrorEnabled = false
                    textInputLayoutSex.error = ""
                }
                is BabyFormState.InvalidSex -> {
                    textInputLayoutName.isErrorEnabled = false
                    textInputLayoutName.error = ""
                    textInputLayoutSex.isErrorEnabled = true
                    textInputLayoutSex.error = getString(formState.errorRes)
                }
                BabyFormState.Valid -> {
                    val baby = Baby(
                        name = etName.text.toString(),
                        birthday = viewModel.currentDate(),
                        sex = autoCompleteSex.text.toString(),
                        photoUri = photoUri,
                    )
                    if (isEditingMode)
                        viewModel.update(baby)
                    else
                        viewModel.saveBaby(baby)

                    openMainActivity()
                }
            }
        }
        viewModel.baby.observe(this) { baby ->
            baby?.let {
                loadBabyInfo(it)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }
}