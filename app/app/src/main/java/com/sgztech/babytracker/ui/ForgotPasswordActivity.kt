package com.sgztech.babytracker.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sgztech.babytracker.R
import com.sgztech.babytracker.extension.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordActivity : BaseActivity() {

    private val progressBar: ProgressBar by lazy { findViewById(R.id.pbForgotPassword) }
    private val btnResetPassword: MaterialButton by lazy { findViewById(R.id.btnResetPassword) }
    private val btnSendRecoveryCode: MaterialButton by lazy { findViewById(R.id.btnSendRecoveryCode) }
    private val textInputLayoutRecoveryCode: TextInputLayout by lazy { findViewById(R.id.textInputLayoutRecoveryCode) }
    private val textInputLayoutEmail: TextInputLayout by lazy { findViewById(R.id.textInputLayoutEmail) }
    private val etEmail: TextInputEditText by lazy { findViewById(R.id.etEmail) }
    private val etRecoveryCode: TextInputEditText by lazy { findViewById(R.id.etRecoveryCode) }
    private val panelRecoveryCode: LinearLayout by lazy { findViewById(R.id.panelRecoveryCode) }
    private val viewModel: ForgotPasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setupToolbar(toolbar, R.string.toolbar_title_forgot_password)
        setupBtnReset()
        setupBtnRecoveryCode()
        observeEvents()
    }

    private fun setupBtnReset() {
        btnResetPassword.setOnClickListener {
            it.hideKeyBoard()
            val email = etEmail.text.toString()
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not())
                textInputLayoutEmail.enableError(R.string.msg_enter_valid_email)
            else {
                textInputLayoutEmail.disableError()
                viewModel.forgotPassword(email)
            }
        }
    }

    private fun setupBtnRecoveryCode() {
        btnSendRecoveryCode.setOnClickListener {
            it.hideKeyBoard()
            val code = etRecoveryCode.text.toString()
            if (code.isEmpty())
                textInputLayoutRecoveryCode.enableError(R.string.et_recovery_code)
            else {
                val email = etEmail.text.toString()
                textInputLayoutRecoveryCode.disableError()
                viewModel.verifyCode(email, code)
            }
        }
    }

    private fun observeEvents() {
        viewModel.forgotAction.observe(this) { action ->
            when (action) {
                is RequestAction.AuthFailure -> {
                    progressBar.hide()
                    btnResetPassword.showSnackbar(action.errorRes)
                }
                is RequestAction.GenericFailure -> {
                    progressBar.hide()
                    btnResetPassword.showSnackbar(action.errorRes)
                }
                RequestAction.Loading -> progressBar.show()
                is RequestAction.Success<*> -> {
                    progressBar.hide()
                    panelRecoveryCode.visible()
                    etEmail.isEnabled = false
                    btnResetPassword.isEnabled = false
                }
                is RequestAction.ValidationFailure -> {
                    progressBar.hide()
                    btnResetPassword.showSnackbar(action.errors.joinToString())
                }
            }
        }

        viewModel.codeAction.observe(this) { action ->
            when (action) {
                is RequestAction.AuthFailure -> {
                    progressBar.hide()
                    btnResetPassword.showSnackbar(action.errorRes)
                }
                is RequestAction.GenericFailure -> {
                    progressBar.hide()
                    btnResetPassword.showSnackbar(action.errorRes)
                }
                RequestAction.Loading -> progressBar.show()
                is RequestAction.Success<*> -> {
                    progressBar.hide()
                    val intent = Intent(this, ResetPasswordActivity::class.java)
                    intent.putExtra(EMAIL_EXTRA_KEY,  etEmail.text.toString())
                    intent.putExtra(CODE_EXTRA_KEY,  etRecoveryCode.text.toString())
                    startActivity(intent)
                    finish()
                }
                is RequestAction.ValidationFailure -> {
                    progressBar.hide()
                    btnResetPassword.showSnackbar(action.errors.joinToString())
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

    companion object {
        const val EMAIL_EXTRA_KEY = "email_extra"
        const val CODE_EXTRA_KEY = "code_extra"
    }
}