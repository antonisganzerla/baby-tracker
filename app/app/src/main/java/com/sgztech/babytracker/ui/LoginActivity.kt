package com.sgztech.babytracker.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.GoogleAuthProvider
import com.sgztech.babytracker.R
import com.sgztech.babytracker.firebaseInstance
import com.sgztech.babytracker.googleSignInClient
import com.sgztech.babytracker.model.User
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val googleSignInActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))
        }
    }

    private val textInputLayoutEmail: TextInputLayout by lazy { findViewById(R.id.textInputLayoutEmail) }
    private val textInputLayoutPassword: TextInputLayout by lazy { findViewById(R.id.textInputLayoutPassword) }
    private val etEmail: TextInputEditText by lazy { findViewById(R.id.etEmail) }
    private val etPassword: TextInputEditText by lazy { findViewById(R.id.etPassword) }
    private val cbRememberMe: MaterialCheckBox by lazy { findViewById(R.id.cbRememberMe) }
    private val signInButton: SignInButton by lazy { findViewById(R.id.sign_in_button) }
    private val loginButton: MaterialButton by lazy { findViewById(R.id.btn_login) }
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupLoginButton()
        setupBtnSignInGoogle()
        setupBtnForgotPassword()
        setupBtnCreateAccount()
        observeEvents()

        viewModel.getRememberMe()?.let {
            etEmail.setText(it.email)
            cbRememberMe.isChecked = it.value
        }
    }

    private fun setupLoginButton() {
        loginButton.setOnClickListener {
            viewModel.validate(
                email = etEmail.text.toString(),
                password = etPassword.text.toString(),
            )
        }
    }

    private fun setupBtnSignInGoogle() {
        val tv = signInButton.getChildAt(0) as TextView
        tv.text = getString(R.string.hint_sign_in_google_login_button).uppercase()
        signInButton.setOnClickListener {
            val signInIntent = googleSignInClient(this).signInIntent
            googleSignInActivityResult.launch(signInIntent)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            log(getString(R.string.msg_signin_fail, e.statusCode.toString()))
            Toast.makeText(this, R.string.msg_signin_fail_snack_bar, Toast.LENGTH_LONG).show()
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    log(getString(R.string.msg_signin_firebase_success))
                    val user = User(
                        id = 1,
                        name = acct.displayName.toString(),
                        email = acct.email.toString(),
                        token = acct.idToken.toString(),
                        photoUri = acct.photoUrl.toString(),
                    )
                    val saveMe = cbRememberMe.isChecked
                    viewModel.saveUser(user, saveMe)
                    viewModel.navigateToNextScreen(user.id)
                } else {
                    log(getString(R.string.msg_signin_firebase_fail))
                    log(task.exception.toString())
                }
            }
    }

    private fun openBabyActivity() {
        val intent = Intent(this, BabyActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupBtnForgotPassword() {
        val tvForgotPassword = findViewById<TextView>(R.id.tv_forgot_password)
        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupBtnCreateAccount() {
        val tvCreateAccount = findViewById<TextView>(R.id.tv_create_account)
        tvCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeEvents() {
        viewModel.formState.observe(this) { formState ->
            when (formState) {
                is LoginFormState.InvalidEmail -> {
                    textInputLayoutEmail.isErrorEnabled = true
                    textInputLayoutEmail.error = getString(formState.errorRes)
                    textInputLayoutPassword.isErrorEnabled = false
                    textInputLayoutPassword.error = ""
                }
                is LoginFormState.InvalidPassword -> {
                    textInputLayoutPassword.isErrorEnabled = true
                    textInputLayoutPassword.error = getString(formState.errorRes)
                    textInputLayoutEmail.isErrorEnabled = false
                    textInputLayoutEmail.error = ""
                }
                LoginFormState.Valid -> {
                    textInputLayoutEmail.isErrorEnabled = false
                    textInputLayoutEmail.error = ""
                    textInputLayoutPassword.isErrorEnabled = false
                    textInputLayoutPassword.error = ""
                    val user = User(
                        id = 1,
                        name = "",
                        email = etEmail.text.toString(),
                        token = "",
                    )
                    val saveMe = cbRememberMe.isChecked
                    viewModel.saveUser(user, saveMe)
                    viewModel.navigateToNextScreen(user.id)
                }
            }
        }

        viewModel.navigateState.observe(this) { navigateState ->
            when(navigateState){
                NavigateState.BabyFormScreen -> openBabyActivity()
                NavigateState.MainScreen -> openMainActivity()
            }
        }
    }

    private fun log(message: String) {
        Log.i("TAG", message)
    }
}