package com.aura.app.data.network

import com.aura.app.data.model.Account
import retrofit2.http.Body
import retrofit2.http.POST
import com.aura.app.data.model.Credentials
import com.aura.app.data.model.CredentialsResult
import com.aura.app.data.model.Transfer
import com.aura.app.data.model.TransferResult
import retrofit2.http.GET
import retrofit2.http.Path


 /**
 * Une interface Retrofit définissant les points de terminaison des services réseau.
 */
interface ServiceInterface {
    @POST("/login")
    suspend fun login(@Body credentials: Credentials): CredentialsResult

    @GET("/accounts/{id}")
    suspend fun getAccountsByUserId(@Path("id") userId: String): List<Account>

    @POST("/transfer")
    suspend fun transfer(@Body transfer: Transfer): TransferResult
}
