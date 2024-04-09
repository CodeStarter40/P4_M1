package com.aura.ui.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.ui.api.ServiceInterface
import com.aura.ui.data.LoginState
import com.aura.ui.model.Credentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val serviceInterface: ServiceInterface) : ViewModel() {
    private val _isFormValid = MutableStateFlow(false)
    val isFormValid = _isFormValid.asStateFlow()

    //mutablestateFlow pour LoginState
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Waiting)
    val loginState = _loginState.asStateFlow()


    //methode pour valider le formulaire
    fun validateForm(identifier: String,password: String){
        val isValide = identifier.isNotEmpty() && password.isNotEmpty() //verif si champs rempli
        _isFormValid.value = isValide
    }

    /**
     * Tente de se connecter avec les identifiants fournis.
     * - Lance un coroutine dans le viewModelScope pour gérer le processus de connexion asynchrone.
     * - Définit le LiveData loginState sur l'état Loading pour indiquer que le processus de connexion a démarré.
     * - Crée un objet Credentials avec l'identifiant et le mot de passe fournis.
     * - Appelle le service de connexion pour authentifier les identifiants.
     * - Si l'authentification est réussie, définit le loginState sur l'état Success, modifie la val dans LoginState, identifier -> userId et affiche un message de réussite.
     * - Si l'authentification échoue, définit le loginState sur l'état Error avec un message d'erreur approprié.
     * - Si une exception se produit pendant le processus de connexion, définit le loginState sur l'état Error avec un message d'erreur générique.
     * @param identifier L'identifiant de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     */
    fun login(identifier: String,password: String){ viewModelScope.launch {
        try {
            _loginState.value = LoginState.Loading
            val credentials = Credentials(id = identifier, password = password)
            val result = serviceInterface.login(credentials)
            if (result.granted){
                //connexion reussie, toast message "Connexion Succès"
                _loginState.value = LoginState.Success(identifier)//recup de l'identifier -> vers userId

            } else {
                //echec place un message d'erreur login fail
                _loginState.value = LoginState.Error("Invalid credentials")
            }
        } catch (e: Exception) {
            _loginState.value = LoginState.Error(e.message ?: "Unknown error")

        }
    }
    }
}