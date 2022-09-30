package com.sgztech.babytracker.ui

import android.app.Activity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.babytracker.R
import com.sgztech.babytracker.extension.*
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
                    textInputLayoutEmail.enableError(formState.errorRes)
                    textInputLayoutFullName.disableError()
                    textInputLayoutPassword.disableError()
                    textInputLayoutConfirmPassword.disableError()
                }
                is RegisterFormState.InvalidName -> {
                    textInputLayoutEmail.disableError()
                    textInputLayoutFullName.enableError(formState.errorRes)
                    textInputLayoutPassword.disableError()
                    textInputLayoutConfirmPassword.disableError()
                }
                is RegisterFormState.InvalidPassword -> {
                    textInputLayoutEmail.disableError()
                    textInputLayoutFullName.disableError()
                    textInputLayoutPassword.enableError(formState.errorRes)
                    textInputLayoutConfirmPassword.disableError()
                }
                is RegisterFormState.InvalidRepeatPassword -> {
                    textInputLayoutEmail.disableError()
                    textInputLayoutFullName.disableError()
                    textInputLayoutPassword.disableError()
                    textInputLayoutConfirmPassword.enableError(formState.errorRes)
                }
                RegisterFormState.Valid -> {
                    textInputLayoutEmail.disableError()
                    textInputLayoutFullName.disableError()
                    textInputLayoutPassword.disableError()
                    textInputLayoutConfirmPassword.disableError()
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
                RequestAction.Loading -> pbRegister.show()
                is RequestAction.Success<*> -> {
                    pbRegister.hide()
                    setResult(RESULT_OK)
                    finish()
                }
                is RequestAction.GenericFailure -> {
                    btnRegister.showSnackbar(action.errorRes)
                    pbRegister.hide()
                }
                is RequestAction.ValidationFailure -> {
                    btnRegister.showSnackbar(action.errors.joinToString())
                    pbRegister.hide()
                }
                is RequestAction.AuthFailure -> {
                    btnRegister.showSnackbar(action.errorRes)
                    pbRegister.hide()
                }
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
}