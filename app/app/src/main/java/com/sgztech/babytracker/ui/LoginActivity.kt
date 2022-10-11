package com.sgztech.babytracker.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
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
import com.sgztech.babytracker.extension.*
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

    private val registerUserActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            loginButton.showSnackbar(R.string.msg_success_register)
        }
    }

    private val pbLogin: ProgressBar by lazy { findViewById(R.id.pb_login) }
    private val textInputLayoutEmail: TextInputLayout by lazy { findViewById(R.id.textInputLayoutEmail) }
    private val textInputLayoutPassword: TextInputLayout by lazy { findViewById(R.id.textInputLayoutPassword) }
    private val etEmail: TextInputEditText by lazy { findViewById(R.id.etEmail) }
    private val etPassword: TextInputEditText by lazy { findViewById(R.id.etPassword) }
    private val cbRememberMe: MaterialCheckBox by lazy { findViewById(R.id.cbRememberMe) }
    private val signInButton: SignInButton by lazy { findViewById(R.id.sign_in_button) }
    private val loginButton: MaterialButton by lazy { findViewById(R.id.btn_login) }
    private val viewModel: LoginViewModel by viewModel()
    private var photoUri: String? = null

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
            it.hideKeyBoard()
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
            logError(getString(R.string.msg_signin_fail, e.statusCode.toString()))
            signInButton.showSnackbar(R.string.msg_signin_fail_snack_bar)
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    logInfo(getString(R.string.msg_signin_firebase_success))
                    photoUri = acct.photoUrl.toString()
                    viewModel.authWithGoogle(
                        name = acct.displayName.toString(),
                        email = acct.email.toString(),
                        token = acct.idToken.toString(),
                    )
                } else {
                    logError(getString(R.string.msg_signin_firebase_fail))
                    logError(task.exception)
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
            registerUserActivityResult.launch(intent)
        }
    }

    private fun observeEvents() {
        viewModel.formState.observe(this) { formState ->
            when (formState) {
                is LoginFormState.InvalidEmail -> {
                    textInputLayoutEmail.enableError(formState.errorRes)
                    textInputLayoutPassword.disableError()
                }
                is LoginFormState.InvalidPassword -> {
                    textInputLayoutEmail.disableError()
                    textInputLayoutPassword.enableError(formState.errorRes)
                }
                LoginFormState.Valid -> {
                    textInputLayoutEmail.disableError()
                    textInputLayoutPassword.disableError()
                    viewModel.auth(
                        email = etEmail.text.toString(),
                        password = etPassword.text.toString(),
                    )
                }
            }
        }

        viewModel.authAction.observe(this) { action ->
            when (action) {
                RequestAction.Loading -> pbLogin.show()
                is RequestAction.Success<*> -> {
                    val saveMe = cbRememberMe.isChecked
                    val user = (action.value as User).copy(photoUri = photoUri)
                    viewModel.saveUserSession(user, saveMe)
                    viewModel.navigateToNextScreen(user.id)
                    pbLogin.hide()
                }
                is RequestAction.GenericFailure -> {
                    pbLogin.hide()
                    loginButton.showSnackbar(action.errorRes)
                }
                is RequestAction.ValidationFailure -> {
                    pbLogin.hide()
                    loginButton.showSnackbar(action.errors.joinToString())
                }
                is RequestAction.AuthFailure -> {
                    pbLogin.hide()
                    loginButton.showSnackbar(action.errorRes)
                }
            }
        }

        viewModel.navigateState.observe(this) { navigateState ->
            when (navigateState) {
                NavigateState.BabyFormScreen -> openBabyActivity()
                NavigateState.MainScreen -> openMainActivity()
                is NavigateState.Failure -> {
                    pbLogin.gone()
                    when (navigateState.error) {
                        is RequestAction.GenericFailure -> loginButton.showSnackbar(navigateState.error.errorRes)
                        is RequestAction.ValidationFailure -> loginButton.showSnackbar(navigateState.error.errors.joinToString())
                        else -> {}
                    }
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