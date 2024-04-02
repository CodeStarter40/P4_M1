package com.aura.ui.login

import android.service.carrier.CarrierIdentifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.ui.api.LoginService
import com.aura.ui.data.LoginState
import com.aura.ui.data.NetworkModule
import com.aura.ui.model.Credentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel: ViewModel() {
    private val _isFormValid = MutableStateFlow(false)
    val isFormValid = _isFormValid.asStateFlow()
    //utilisation de Networkmodule pour creer une instance de LoginService
    private val loginService: LoginService = NetworkModule.retrofit.create(LoginService::class.java)

    //mutablestateFlow pour LoginState
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    val loginState = _loginState.asStateFlow()


    //methode pour valider le formulaire
    fun validateForm(identifier: String,password: String){
        val isValide = identifier.isNotEmpty() && password.isNotEmpty() //verif si champs rempli
        _isFormValid.value = isValide
    }

    fun login(identifier: String,password: String){ viewModelScope.launch {
        try {
            _loginState.value = LoginState.Loading
            val credentials = Credentials(id = identifier, password = password)
            val result = loginService.login(credentials)
            if (result.granted){
                //connexion reussie, toast message "Connexion reussie"
                _loginState.value = LoginState.Success

            } else {
                //echec place un message d'erreur login fail
                _loginState.value = LoginState.Error("Connexion Failed")
            }
        } catch (e: Exception) {
            _loginState.value = LoginState.Error(e.message ?: "Unknown error")

        }
    }
    }

}