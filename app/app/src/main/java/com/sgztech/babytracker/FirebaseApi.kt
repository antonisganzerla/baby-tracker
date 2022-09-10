package com.sgztech.babytracker

import com.google.firebase.auth.FirebaseAuth

fun firebaseInstance(): FirebaseAuth =
    FirebaseAuth.getInstance()