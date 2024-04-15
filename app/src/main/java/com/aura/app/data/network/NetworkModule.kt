package com.aura.app.data.network

import com.aura.BuildConfig
import com.aura.app.data.repository.BankRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 *Module dagger hilt qui fournit les dépendances pour les opérations réseau.
 *installé dans SingletonComponent pour une instance unique dans l'application.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val contentType = "application/json".toMediaType()

    private val json = Json { ignoreUnknownKeys = true }

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
    }.build()

    /**
     *fournit une instance de Retrofit pour les appels réseau. ( SERVER renseigné dans le Gradle )
     */
    @Provides
    @Singleton
    @OptIn(ExperimentalSerializationApi::class)
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER)
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(okHttpClient)
        .build()

    /**
     *fournit une instance de ServiceInterface pour les appels d'authentification et de transfert.
     */
    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): ServiceInterface = retrofit.create(ServiceInterface::class.java)

    /**
     *fournit une instance du Repo pour la gestion des données.
     */
    @Provides
    @Singleton
    fun provideRepository(serviceInterface: ServiceInterface): BankRepository = BankRepository(serviceInterface)
}

