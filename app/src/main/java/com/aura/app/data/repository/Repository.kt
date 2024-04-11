package com.aura.app.data.repository

import com.aura.app.data.model.Account
import com.aura.app.data.model.Credentials
import com.aura.app.data.model.CredentialsResult
import com.aura.app.data.network.ServiceInterface
import javax.inject.Inject

/**
 * Effectue une opération de connexion de l'utilisateur en effectuant une requête réseau à l'aide des [credentials] fournis.
 *
 * @param credentials Les informations d'identification de l'utilisateur pour l'authentification.
 * @return Un objet [CredentialsResult] représentant le résultat de l'opération de connexion.
 */

class Repository @Inject constructor(private val serviceInterface: ServiceInterface) {

    suspend fun login(credentials: Credentials):CredentialsResult { return serviceInterface.login(credentials) }

    suspend fun getAccountsByUserId(userId: String): List<Account> { return serviceInterface.getAccountsByUserId(userId) }

}