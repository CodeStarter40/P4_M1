package com.aura.app.ui.login


import retrofit2.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.app.data.model.Credentials
import com.aura.app.data.state.LoginState
import com.aura.app.data.repository.BankRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.Exception


@HiltViewModel
class LoginViewModel @Inject constructor(private val bankRepository: BankRepository) : ViewModel() {
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
     * Attempts to log in with the provided credentials.
     * - Launches a coroutine within the viewModelScope to handle the asynchronous login process.
     * - Sets the loginState LiveData to Loading to indicate that the login process has started.
     * - Creates a Credentials object with the provided identifier and password.
     * - Calls the login service to authenticate the credentials.
     * - If authentication is successful, sets the loginState to Success, updates the userId in LoginState, and displays a success message.
     * - If authentication fails, sets the loginState to Error with an appropriate error message.
     * - If an exception occurs during the login process, sets the loginState to Error with a generic error message.
     * @param identifier The user's identifier.
     * @param password The user's password
     * ////////////////////////
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
            val result = bankRepository.login(credentials)
            if (result.granted){
                //connexion reussie, toast message "Connexion Succès"
                _loginState.value = LoginState.Success(identifier)//recup de l'identifier -> vers userId

            } else {
                //echec place un message d'erreur login fail
                _loginState.value = LoginState.Error("Invalid credentials")
            }
        } catch (e: Exception) {
            _loginState.value = LoginState.Error(e.message ?: "Unknown error")

        } catch (e:HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "Unauthorized or session expired."
                403 -> "Access denied."
                404 -> "User account not found."
                else -> "Connexion Error : ${e.code()}"
            }
            _loginState.value = LoginState.Error(errorMessage)
        } catch (e:IOException) {
            //network connexion problem
            _loginState.value = LoginState.Error("Problème de connexion réseau")

        } catch (e:Exception) {
            //unknow error
            _loginState.value = LoginState.Error(e.message ?:"Une erreur inconnue est survenue")
        }
    }
    }
}