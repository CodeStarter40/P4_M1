package com.aura.app.ui.home

import retrofit2.HttpException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.app.data.model.Account
import com.aura.app.data.repository.BankRepository
import com.aura.app.data.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel for the Home screen
 * //////////////
 * ViewModel pour l'écran d'accueil
 */

@HiltViewModel
class HomeViewModel @Inject constructor(private val bankRepository: BankRepository) : ViewModel()  {
    //mutableStateFlow pour stocker la liste des comptes, initialisé avec une liste vide
    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    //stateFlow immuable exposant la liste des comptes en lecture seule
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Waiting)
    val loginState = _loginState.asStateFlow()

    /**
     * Fetches the user's accounts based on the provided user ID.
     * @param userId The ID of the user whose accounts need to be fetched.
     * /////////////////
     * Récupère les comptes de l'utilisateur en fonction de l'ID utilisateur fourni.
     * @param userId L'ID de l'utilisateur dont les comptes doivent être récupérés.
     */

    fun fetchAccountUser(userId: String) {
        viewModelScope.launch {//asynch
            try {
                val accountsList = bankRepository.getAccountsByUserId(userId) //récup de la liste des comptes de l'utilisateur à partir du service
                _accounts.value = accountsList //maj account dans le mutableflow
                Log.d("FETCHACCOUNTUSER","Account récupéré avec succès")
            } catch (e: Exception) {
                //affiche un message générique en cas d'exeption
                Log.e(
                    "FETCHACCOUNTUSER", "Une erreur s'est produite lors de la récupération des comptes")
            } catch (e: HttpException) {
                val errorMessage = when (e.code()) {
                    401 -> "Unauthorized or session expired."
                    403 -> "Access denied for account data."
                    404 -> "User account not found."
                    else -> "Connexion Error : ${e.code()}"
                }
                // _loginState.value = loginState.error(errorMessage)
            }
        }
    }

    /**
     * Refreshes the user's accounts data with the provided user ID.
     * @param userId The ID of the user whose accounts need to be refreshed.
     * //////
     * Rafraîchit les données des comptes de l'utilisateur avec l'ID utilisateur fourni.
     * @param userId L'ID de l'utilisateur dont les comptes doivent être rafraîchis.
     */
    fun refreshAccount(userId: String) {
        viewModelScope.launch {
            try {
                val accountsList = bankRepository.getAccountsByUserId(userId)
                _accounts.value = accountsList
                Log.d("REFRESH ACCOUNT", "Accounts refreshed successfully")
            } catch (e: Exception) {
                Log.e("REFRESH ACCOUNT", "An error occurred while refreshing the account")
            }
        }
    }
}
