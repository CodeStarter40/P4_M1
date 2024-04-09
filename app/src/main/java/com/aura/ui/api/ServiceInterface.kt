package com.aura.ui.api

import com.aura.ui.model.Account
import retrofit2.http.Body
import retrofit2.http.POST
import com.aura.ui.model.Credentials
import com.aura.ui.model.CredentialsResult
import retrofit2.http.GET
import retrofit2.http.Path

interface ServiceInterface {
    @POST("/login")
    suspend fun login(@Body credentials: Credentials): CredentialsResult

    @GET("/accounts/{id}")
    suspend fun getAccountsByUserId(@Path("id") userId: String): List<Account>

}
