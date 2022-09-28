package com.sgztech.babytracker.di

import androidx.preference.PreferenceManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sgztech.babytracker.BuildConfig
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.data.BabyRepository
import com.sgztech.babytracker.data.RegisterRepository
import com.sgztech.babytracker.data.RegisterUserRepository
import com.sgztech.babytracker.database.AppDatabase
import com.sgztech.babytracker.service.ServiceExecutor
import com.sgztech.babytracker.service.UserApi
import com.sgztech.babytracker.service.UserService
import com.sgztech.babytracker.ui.*
import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

private val contentType = "application/json".toMediaType()
private const val BASE_URL = "http://192.168.0.106:8080/api/"

@OptIn(ExperimentalSerializationApi::class)
val serviceModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor())
            .build()
    }

    factory {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    factory {
        val client: OkHttpClient = get()
        val json: Json = get()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .addConverterFactory(ApiResultConverterFactory)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    factory {
        val retrofit: Retrofit = get()
        retrofit.create(UserApi::class.java)
    }

    factory {
        ServiceExecutor()
    }

    factory {
        UserService(get(), get())
    }
}

val dbModule = module {
    single {
        AppDatabase.getInstance(context = get())
    }
    factory { get<AppDatabase>().registerDao() }
    factory { get<AppDatabase>().babyDao() }
}

val repositoryModule = module {
    factory { RegisterRepository(get()) }
    factory { BabyRepository(get()) }
    factory { RegisterUserRepository(get()) }
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
    viewModel { RegisterUserViewModel(get()) }
}

private fun loggingInterceptor(): Interceptor =
    HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }