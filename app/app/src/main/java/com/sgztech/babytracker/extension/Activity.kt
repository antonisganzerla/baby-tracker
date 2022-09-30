package com.sgztech.babytracker.extension

import android.app.Activity
import android.view.WindowManager

fun Activity.disableUserInteraction(){
    window.disableUserInteraction()
}

fun Activity.enableUserInteraction(){
    window.enableUserInteraction()
}