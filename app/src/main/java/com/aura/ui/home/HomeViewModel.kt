package com.aura.ui.home

import android.net.http.HttpException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.ui.api.LoginService
import com.aura.ui.model.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import javax.inject.Inject

/**
 * ViewModel pour gérer la logique métier de la page d'accueil de l'application.
 *
 * Cette classe est annotée avec @HiltViewModel pour permettre l'injection de dépendances
 * via Hilt, ce qui facilite l'injection de la dépendance [LoginService].
 *
 * @param loginService Service responsable de la gestion de l'authentification et de la récupération des comptes.
 */

@HiltViewModel
class HomeViewModel @Inject constructor(private val loginService: LoginService) : ViewModel()  {
    //mutableStateFlow pour stocker la liste des comptes, initialisé avec une liste vide
    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    //stateFlow immuable exposant la liste des comptes en lecture seule
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()

    /**
     * Méthode pour récupérer les comptes utilisateur à partir de l'identifiant de l'utilisateur.
     *
     * Cette méthode utilise le CoroutineScope du ViewModel pour lancer une coroutine afin de récupérer
     * les comptes de manière asynchrone depuis le [LoginService]. En cas d'erreur, elle logge
     * l'erreur avec un message générique.
     *
     * @param userId L'identifiant de l'utilisateur pour lequel récupérer les comptes.
     */

    fun fetchAccountUser(userId: String) {
        viewModelScope.launch { //asynch
            try {
                val accountsList = loginService.getAccountsByUserId(userId) //récup de la liste des comptes de l'utilisateur à partir du service
                _accounts.value = accountsList //maj account dans le mutableflow
                Log.d("HomeViewModel","Account récupéré avec succès")
            } catch (e: Exception) {
                //affiche un message génériqueen cas d'exeption
                Log.e("HomeViewModel", "Une erreur s'est produite lors de la récupération des comptes", e)
            } //catch (e:HttpException) {
                //Log.e("HommeViewModel","HTTP Erreur lors de la récuperation de compte")
            //}
        }
    }
}
