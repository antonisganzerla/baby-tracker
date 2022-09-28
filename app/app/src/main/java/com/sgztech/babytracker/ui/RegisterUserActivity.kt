package com.sgztech.babytracker.ui

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.babytracker.R
import com.sgztech.babytracker.extension.gone
import com.sgztech.babytracker.extension.hideKeyBoard
import com.sgztech.babytracker.extension.showSnackbar
import com.sgztech.babytracker.extension.visible
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterUserActivity : BaseActivity() {

    private val pbRegister: ProgressBar by lazy { findViewById(R.id.pbRegister) }
    private val textInputLayoutFullName: TextInputLayout by lazy { findViewById(R.id.textInputLayoutFullName) }
    private val textInputLayoutEmail: TextInputLayout by lazy { findViewById(R.id.textInputLayoutEmail) }
    private val textInputLayoutPassword: TextInputLayout by lazy { findViewById(R.id.textInputLayoutPassword) }
    private val textInputLayoutConfirmPassword: TextInputLayout by lazy { findViewById(R.id.textInputLayoutConfirmPassword) }
    private val etFullName: TextInputEditText by lazy { findViewById(R.id.etFullName) }
    private val etEmail: TextInputEditText by lazy { findViewById(R.id.etEmail) }
    private val etPassword: TextInputEditText by lazy { findViewById(R.id.etPassword) }
    private val etConfirmPassword: TextInputEditText by lazy { findViewById(R.id.etConfirmPassword) }
    private val btnRegister: MaterialButton by lazy { findViewById(R.id.btn_register) }
    private val viewModel: RegisterUserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        setupToolbar()
        setupButton()
        observeEvents()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setupToolbar(toolbar, R.string.toolbar_title_register)
    }

    private fun setupButton() {
        btnRegister.setOnClickListener {
            viewModel.validate(
                name = etFullName.text.toString(),
                email = etEmail.text.toString(),
                password = etPassword.text.toString(),
                repeatPassword = etConfirmPassword.text.toString(),
            )
            it.hideKeyBoard()
        }
    }

    private fun observeEvents() {
        viewModel.formState.observe(this) { formState ->
            when (formState) {
                is RegisterFormState.InvalidEmail -> {
                    textInputLayoutEmail.isErrorEnabled = true
                    textInputLayoutEmail.error = getString(formState.errorRes)
                    textInputLayoutFullName.isErrorEnabled = false
                    textInputLayoutFullName.error = ""
                    textInputLayoutPassword.isErrorEnabled = false
                    textInputLayoutPassword.error = ""
                    textInputLayoutConfirmPassword.isErrorEnabled = false
                    textInputLayoutConfirmPassword.error = ""
                }
                is RegisterFormState.InvalidName -> {
                    textInputLayoutFullName.isErrorEnabled = true
                    textInputLayoutFullName.error = getString(formState.errorRes)
                    textInputLayoutEmail.isErrorEnabled = false
                    textInputLayoutEmail.error = ""
                    textInputLayoutPassword.isErrorEnabled = false
                    textInputLayoutPassword.error = ""
                    textInputLayoutConfirmPassword.isErrorEnabled = false
                    textInputLayoutConfirmPassword.error = ""
                }
                is RegisterFormState.InvalidPassword -> {
                    textInputLayoutPassword.isErrorEnabled = true
                    textInputLayoutPassword.error = getString(formState.errorRes)
                    textInputLayoutEmail.isErrorEnabled = false
                    textInputLayoutEmail.error = ""
                    textInputLayoutFullName.isErrorEnabled = false
                    textInputLayoutFullName.error = ""
                    textInputLayoutConfirmPassword.isErrorEnabled = false
                    textInputLayoutConfirmPassword.error = ""
                }
                is RegisterFormState.InvalidRepeatPassword -> {
                    textInputLayoutConfirmPassword.isErrorEnabled = true
                    textInputLayoutConfirmPassword.error = getString(formState.errorRes)
                    textInputLayoutEmail.isErrorEnabled = false
                    textInputLayoutEmail.error = ""
                    textInputLayoutFullName.isErrorEnabled = false
                    textInputLayoutFullName.error = ""
                    textInputLayoutPassword.isErrorEnabled = false
                    textInputLayoutPassword.error = ""
                }
                RegisterFormState.Valid -> {
                    viewModel.register(
                        name = etFullName.text.toString(),
                        email = etEmail.text.toString(),
                        password = etPassword.text.toString(),
                    )
                }
            }
        }

        viewModel.registerAction.observe(this) { action ->
            when (action) {
                RegisterAction.Loading -> pbRegister.visible()
                RegisterAction.Success -> {
                    btnRegister.showSnackbar(R.string.msg_success_register)
                    // TODO criar dialog
                    pbRegister.gone()
                    finish()
                }
                is RegisterAction.UnknownFailure -> {
                    btnRegister.showSnackbar(action.errorRes)
                    pbRegister.gone()
                }
                is RegisterAction.ValidationFailure -> {
                    btnRegister.showSnackbar(action.errors.joinToString())
                    pbRegister.gone()
                }
            }
        }
    }
}