package com.aura.app.ui.login


import retrofit2.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.app.data.model.Credentials
import com.aura.app.data.model.ResultState
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

    //mutablestateFlow pour ResultState
    private val _resultState = MutableStateFlow<ResultState>(ResultState.Waiting)
    val loginState = _resultState.asStateFlow()


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
     * - Si l'authentification est réussie, définit le loginState sur l'état Success, modifie la val dans ResultState, identifier -> userId et affiche un message de réussite.
     * - Si l'authentification échoue, définit le loginState sur l'état Error avec un message d'erreur approprié.
     * - Si une exception se produit pendant le processus de connexion, définit le loginState sur l'état Error avec un message d'erreur générique.
     * @param identifier L'identifiant de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     */
    fun login(identifier: String,password: String){ viewModelScope.launch {
        try {
            _resultState.value = ResultState.Loading
            val credentials = Credentials(id = identifier, password = password)
            val result = bankRepository.login(credentials)
            if (result.granted){
                //connexion reussie, toast message "Connexion Succès"
                _resultState.value = ResultState.Success(identifier)//recup de l'identifier -> vers userId

            } else {
                //echec place un message d'erreur login fail
                _resultState.value = ResultState.Error("Invalid credentials")
            }
        } catch (e: Exception) {
            _resultState.value = ResultState.Error(e.message ?: "Unknown error")

        } catch (e:HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "Non autorisé. Vérifiez vos identifiants."
                403 -> "Accès refusé."
                404 -> "Service non trouvé."
                else -> "Erreur de connexion : ${e.code()}"
            }
            _resultState.value = ResultState.Error(errorMessage)
        } catch (e:IOException) {
            //network connexion problem
            _resultState.value = ResultState.Error("Problème de connexion réseau")

        } catch (e:Exception) {
            //unknow error
            _resultState.value = ResultState.Error(e.message ?:"Une erreur inconnue est survenue")
        }
    }
    }
}