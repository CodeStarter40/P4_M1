package com.aura.ui.api

import retrofit2.http.Body
import retrofit2.http.POST
import com.aura.ui.model.Credentials
import com.aura.ui.model.CredentialsResult

interface LoginService {
    @POST("/login")
    suspend fun login(@Body credentials: Credentials): CredentialsResult
}