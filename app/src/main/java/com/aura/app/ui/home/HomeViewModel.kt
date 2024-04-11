package com.aura.app.ui.home

import retrofit2.HttpException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.app.data.network.ServiceInterface
import com.aura.app.data.model.Account
import com.aura.app.data.repository.Repository
import com.aura.app.ui.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel()  {
    //mutableStateFlow pour stocker la liste des comptes, initialisé avec une liste vide
    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    //stateFlow immuable exposant la liste des comptes en lecture seule
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Waiting)
    val loginState = _loginState.asStateFlow()



    fun fetchAccountUser(userId: String) {
        viewModelScope.launch {//asynch
            try {
                val accountsList = repository.getAccountsByUserId(userId) //récup de la liste des comptes de l'utilisateur à partir du service
                _accounts.value = accountsList //maj account dans le mutableflow
                Log.d("HomeViewModel","Account récupéré avec succès")
            } catch (e: Exception) {
                //affiche un message générique en cas d'exeption
                Log.e(
                    "HomeViewModel",
                    "Une erreur s'est produite lors de la récupération des comptes"
                )
            } catch (e: HttpException) {
                val errorMessage = when (e.code()) {
                    401 -> "Non autorisé ou session expirée."
                    403 -> "Accès refusé pour les données de compte."
                    404 -> "Comptes utilisateurs non trouvés."
                    else -> "Erreur de connexion : ${e.code()}"
                }
                //_loginState.value = LoginState.error(errorMessage)
            }
        }
    }
}
