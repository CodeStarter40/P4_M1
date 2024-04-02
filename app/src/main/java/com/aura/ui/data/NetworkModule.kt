package com.aura.ui.data

import com.aura.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


object NetworkModule {
    private val contentType = "application/json".toMediaType()

    private val json = Json { ignoreUnknownKeys = true }

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
    }.build()

    @OptIn(ExperimentalSerializationApi::class)
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER)
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(okHttpClient)
        .build()
}
