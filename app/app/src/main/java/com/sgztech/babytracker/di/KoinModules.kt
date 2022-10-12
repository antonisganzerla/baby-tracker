package com.sgztech.babytracker.di

import androidx.preference.PreferenceManager
import com.google.firebase.storage.FirebaseStorage
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sgztech.babytracker.BuildConfig
import com.sgztech.babytracker.PreferenceService
import com.sgztech.babytracker.data.*
import com.sgztech.babytracker.database.AppDatabase
import com.sgztech.babytracker.service.*
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
import java.util.concurrent.TimeUnit

private val contentType = "application/json".toMediaType()
private const val BASE_URL_DEV = "http://192.168.0.106:8080/api/"
private const val BASE_URL_PRD = "https://spring-baby-tracker.herokuapp.com/api/"
private const val READ_TIMEOUT = 30_000L
private const val CONNECT_TIMEOUT = 30_000L


@OptIn(ExperimentalSerializationApi::class)
val serviceModule = module {
    single {
        OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
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
            .baseUrl(baseUrl())
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
        val retrofit: Retrofit = get()
        retrofit.create(BabyApi::class.java)
    }

    factory {
        val retrofit: Retrofit = get()
        retrofit.create(RegisterApi::class.java)
    }

    factory {
        ServiceExecutor()
    }

    factory {
        UserService(get(), get())
    }

    factory {
        BabyService(get(), get(), get())
    }

    factory {
        RegisterService(get(), get(), get())
    }

    factory { FirebaseStorage.getInstance() }
    factory { ServiceFirebaseStorage(get()) }
}

val dbModule = module {
    single {
        AppDatabase.getInstance(context = get())
    }
    factory { get<AppDatabase>().registerDao() }
    factory { get<AppDatabase>().babyDao() }
}

val repositoryModule = module {
    factory { RegisterRepository(get(), get()) }
    factory { BabyRepository(get(), get()) }
    factory { RegisterUserRepository(get()) }
    factory { AuthRepository(get()) }
    factory { PasswordRepository(get()) }
}

val uiModule = module {
    factory {
        PreferenceService(sharedPreferences = PreferenceManager.getDefaultSharedPreferences(get()))
    }
    factory { DateTimeFormatter() }
    viewModel { SplashViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get()) }
    viewModel { BabyViewModel(get(), get(), get(), get()) }
    viewModel { MainViewModel(get(), get(), get(), get()) }
    viewModel { RegisterUserViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { ResetPasswordViewModel(get()) }
    viewModel { ChartsViewModel(get(), get()) }
}

private fun loggingInterceptor(): Interceptor =
    HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

private fun baseUrl(): String =
    if (BuildConfig.DEBUG)
        BASE_URL_DEV
    else
        BASE_URL_PRD
