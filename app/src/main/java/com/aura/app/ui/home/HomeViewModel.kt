package com.aura.app.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.app.data.network.ServiceInterface
import com.aura.app.data.model.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class HomeViewModel @Inject constructor(private val serviceInterface: ServiceInterface) : ViewModel()  {
    //mutableStateFlow pour stocker la liste des comptes, initialisé avec une liste vide
    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    //stateFlow immuable exposant la liste des comptes en lecture seule
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()



    fun fetchAccountUser(userId: String) {
        viewModelScope.launch { //asynch
            try {
                val accountsList = serviceInterface.getAccountsByUserId(userId) //récup de la liste des comptes de l'utilisateur à partir du service
                _accounts.value = accountsList //maj account dans le mutableflow
                Log.d("HomeViewModel","Account récupéré avec succès")
            } catch (e: Exception) {
                //affiche un message générique en cas d'exeption
                Log.e("HomeViewModel", "Une erreur s'est produite lors de la récupération des comptes", e)
            } //catch (e:HttpException) {
                //Log.e("HomeViewModel","HTTP Erreur lors de la récuperation de compte")
            //}
        }
    }
}
