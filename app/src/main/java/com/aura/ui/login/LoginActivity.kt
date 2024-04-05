package com.aura.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.aura.databinding.ActivityLoginBinding
import com.aura.ui.home.HomeActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import android.widget.Toast
import com.aura.ui.data.LoginState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

  private lateinit var binding: ActivityLoginBinding
  private lateinit var viewModel: LoginViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

    setupTextWatchers()
    setupLoginStateObserver()
    setupLoginButton()
  }

  /**
    * Met en place des écouteurs de changement de texte pour les champs d'identifiant et de mot de passe.
    * Appelle viewModel.validateForm() à chaque fois que le texte change dans l'un des champs,
    * en passant les valeurs actuelles des champs d'identifiant et de mot de passe.
    * Cela permet au ViewModel d'effectuer une validation de formulaire.
   */
  private fun setupTextWatchers() {
    binding.identifier.addTextChangedListener {
      viewModel.validateForm(
        binding.identifier.text.toString(),
        binding.password.text.toString()
      )
    }
    binding.password.addTextChangedListener {
      viewModel.validateForm(
        binding.identifier.text.toString(),
        binding.password.text.toString()
      )
    }
  }

  /**
    Met en place un observateur pour écouter les changements dans le LiveData de l'état de connexion.
    Utilise lifecycleScope pour lancer une coroutine qui collecte le LiveData de l'état de connexion depuis le ViewModel.
    Appelle handleLoginState() avec l'état reçu pour gérer les mises à jour de l'interface utilisateur en fonction de l'état de connexion.
   */
  private fun setupLoginStateObserver() {
    lifecycleScope.launch {
      viewModel.loginState.collect { state ->
        handleLoginState(state)
      }
    }
  }

  /**
   * Gère les différents états du processus de connexion et met à jour l'interface utilisateur en conséquence.
   * @param state L'état actuel du processus de connexion.
   * Différents indicateurs de chargement pour chaque état :
   * En attente : Masque l'indicateur de chargement.
   * Chargement : Affiche l'indicateur de chargement.
   * Succès : Affiche l'indicateur de chargement, affiche un message de succès et navigue vers l'écran d'accueil.
   * Erreur : Masque l'indicateur de chargement et affiche un message d'erreur.
   */
  private fun handleLoginState(state: LoginState) {
    when (state) {
      is LoginState.Waiting -> binding.loading.visibility = View.GONE
      is LoginState.Loading -> binding.loading.visibility = View.VISIBLE
      is LoginState.Success -> {
        binding.loading.visibility = View.VISIBLE
        Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()
        navigateToHome()
      }
      is LoginState.Error -> {
        binding.loading.visibility = View.GONE
        showError(state.message)
      }
    }
  }

  /**
    * Met en place un écouteur de clic sur le bouton de connexion et observe les changements de validation du formulaire.
    * Lorsque le bouton de connexion est cliqué, il récupère l'identifiant et le mot de passe saisis,
    * invoque la fonction de connexion dans le ViewModel avec ces identifiants et efface les champs de saisie.
    * Il observe les changements de l'état de validation du formulaire et active/désactive le bouton de connexion en conséquence.
   */
  private fun setupLoginButton() { // envoi des id / pw à la fonction login
    binding.login.setOnClickListener {
      val identifier = binding.identifier.text.toString()
      val password = binding.password.text.toString()
      viewModel.login(identifier, password)
      binding.identifier.text.clear()
      binding.password.text.clear()
    }

    //observe form validation change
    lifecycleScope.launch {
      viewModel.isFormValid.collect { isValid ->
        binding.login.isEnabled = isValid
      }
    }
  }

  /**
    * Navigue l'utilisateur vers l'écran d'accueil.
    * Définit la visibilité du chargement sur "visible" pour indiquer une transition.
    * Lance l'activité HomeActivity et termine l'activité actuelle.
   */
  private fun navigateToHome() {
    binding.loading.visibility = View.VISIBLE
    startActivity(Intent(this, HomeActivity::class.java))
    finish()
  }

  /**
   * Affiche un message d'erreur à l'utilisateur.
   * Définit la visibilité du chargement sur "gone" pour masquer l'indicateur de chargement.
   * Affiche un message Toast avec le message d'erreur fourni.
   * @param message Le message d'erreur à afficher.
   */
  private fun showError(message: String) {
    binding.loading.visibility = View.GONE
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }
}
