package com.sgztech.babytracker.extension

import android.view.Window
import android.view.WindowManager

fun Window.disableUserInteraction(){
    setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
    )
}

fun Window.enableUserInteraction(){
    clearFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
    )
}