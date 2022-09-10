package com.sgztech.babytracker

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

fun googleSignInClient(context: Context): GoogleSignInClient =
    GoogleSignIn.getClient(context, googleSignInOptions(getIdToken(context)))

fun googleSignInOptions(idToken: String): GoogleSignInOptions =
    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(idToken)
        .requestEmail()
        .build()

fun getIdToken(context: Context): String =
    context.getString(R.string.default_web_client_id)

