package com.sgztech.babytracker.di

import androidx.preference.PreferenceManager
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.data.RegisterRepository
import com.sgztech.babytracker.database.AppDatabase
import com.sgztech.babytracker.ui.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dbModule = module {
    single {
        AppDatabase.getInstance(context = get())
    }
    factory { get<AppDatabase>().registerDao() }
    factory { get<AppDatabase>().babyDao() }
}

val repositoryModule = module {
    single { RegisterRepository(get()) }
}

val uiModule = module {
    factory {
        PreferenceService(sharedPreferences = PreferenceManager.getDefaultSharedPreferences(get()))
    }
    factory { DateTimeFormatter() }
    viewModel { SplashViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { BabyViewModel(get(), get(), get()) }
    viewModel { MainViewModel(get(), get(), get(), get()) }
}