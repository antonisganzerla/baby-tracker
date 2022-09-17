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
import com.google.firebase.auth.GoogleAuthProvider
import com.sgztech.babytracker.R
import com.sgztech.babytracker.firebaseInstance
import com.sgztech.babytracker.googleSignInClient

class LoginActivity : AppCompatActivity() {

    private val googleSignInActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))
        }
    }

    private val signInButton: SignInButton by lazy { findViewById(R.id.sign_in_button) }
    private val loginButton: MaterialButton by lazy { findViewById(R.id.btn_login) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupLoginButton()
        setupBtnSignInGoogle()
        setupBtnForgotPassword()
        setupBtnCreateAccount()
    }

    private fun setupLoginButton() {
        loginButton.setOnClickListener {
            openMainActivity()
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
                    openMainActivity()
                } else {
                    log(getString(R.string.msg_signin_firebase_fail))
                    log(task.exception.toString())
                }
            }
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
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun log(message: String) {
        Log.i("TAG", message)
    }
}