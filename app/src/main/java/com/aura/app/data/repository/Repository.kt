package com.aura.app.data.repository

import com.aura.app.data.model.Account
import com.aura.app.data.model.Credentials
import com.aura.app.data.model.CredentialsResult
import com.aura.app.data.model.Transfer
import com.aura.app.data.model.TransferResult
import com.aura.app.data.network.ServiceInterface
import javax.inject.Inject


/**
 * Gère les opérations de données telles que l'authentification et la récupération de comptes.
 * Utilise [ServiceInterface] pour les interactions réseau.
 */


class Repository @Inject constructor(private val serviceInterface: ServiceInterface) {

    /**
     * Tente de connecter l'utilisateur en utilisant les informations d'identification fournies.
     * @param credentials Les informations d'identification de l'utilisateur.
     * @return Résultat de la tentative de connexion.
     */
    suspend fun login(credentials: Credentials):CredentialsResult { return serviceInterface.login(credentials) }

    /**
     * Récupère la liste des comptes associés à un utilisateur.
     * @param userId L'identifiant de l'utilisateur.
     * @return Liste des comptes de l'utilisateur.
     */

    suspend fun getAccountsByUserId(userId: String): List<Account> { return serviceInterface.getAccountsByUserId(userId) }


    suspend fun transfer(transfer: Transfer): TransferResult { return serviceInterface.transfer(transfer)}

}