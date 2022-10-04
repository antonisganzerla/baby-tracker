package com.sgztech.babytracker.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.babytracker.R
import com.sgztech.babytracker.extension.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordActivity : BaseActivity() {

    private val progressBar: ProgressBar by lazy { findViewById(R.id.pbResetPassword) }
    private val btnSavePassword: MaterialButton by lazy { findViewById(R.id.btnSavePassword) }
    private val textInputLayoutPassword: TextInputLayout by lazy { findViewById(R.id.textInputLayoutPassword) }
    private val etPassword: TextInputEditText by lazy { findViewById(R.id.etPassword) }
    private val textInputLayoutConfirmPassword: TextInputLayout by lazy { findViewById(R.id.textInputLayoutConfirmPassword) }
    private val etConfirmPassword: TextInputEditText by lazy { findViewById(R.id.etConfirmPassword) }
    private var email: String = ""
    private var code: String = ""
    private val viewModel: ResetPasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setupToolbar(toolbar, R.string.toolbar_title_register_new_password)
        setupBtnSavePassword()
        observeEvents()

        intent.extras?.let {
            email = it.getString(ForgotPasswordActivity.EMAIL_EXTRA_KEY).toString()
            code = it.getString(ForgotPasswordActivity.CODE_EXTRA_KEY).toString()
        }
    }

    private fun setupBtnSavePassword() {
        btnSavePassword.setOnClickListener {
            it.hideKeyBoard()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            when {
                password.length < 8 -> {
                    textInputLayoutPassword.enableError(R.string.msg_enter_valid_password)
                    textInputLayoutConfirmPassword.disableError()
                }
                password != confirmPassword -> {
                    textInputLayoutConfirmPassword.enableError(R.string.msg_password_not_match)
                    textInputLayoutPassword.disableError()
                }
                else -> {
                    textInputLayoutPassword.disableError()
                    textInputLayoutConfirmPassword.disableError()
                    viewModel.passwordReset(
                        email = email,
                        code = code,
                        password = password,
                        confirmPassword = confirmPassword,
                    )
                }
            }
        }
    }

    private fun observeEvents() {
        viewModel.resetAction.observe(this) { action ->
            when (action) {
                is RequestAction.AuthFailure -> {
                    progressBar.hide()
                    btnSavePassword.showSnackbar(action.errorRes)
                }
                is RequestAction.GenericFailure -> {
                    progressBar.hide()
                    btnSavePassword.showSnackbar(action.errorRes)
                }
                RequestAction.Loading -> progressBar.show()
                is RequestAction.Success<*> -> {
                    progressBar.hide()
                    btnSavePassword.showSnackbar(R.string.msg_password_reseted)
                    btnSavePassword.isEnabled = false
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    }, 1500)
                }
                is RequestAction.ValidationFailure -> {
                    progressBar.hide()
                    btnSavePassword.showSnackbar(action.errors.joinToString())
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