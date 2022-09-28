package com.sgztech.babytracker.core

import android.app.Application
import com.sgztech.babytracker.di.dbModule
import com.sgztech.babytracker.di.repositoryModule
import com.sgztech.babytracker.di.serviceModule
import com.sgztech.babytracker.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CoreApplication)
            modules(listOf(dbModule, repositoryModule, uiModule, serviceModule))
        }
    }
}