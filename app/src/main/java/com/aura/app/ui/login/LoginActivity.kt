package com.aura.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.aura.databinding.ActivityLoginBinding
import com.aura.app.ui.home.HomeActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.widget.Toast
import com.aura.app.data.state.LoginState
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


  private fun setupLoginStateObserver() {
    lifecycleScope.launch {
      viewModel.loginState.collect { state -> handleLoginState(state) }
    }
  }


  private fun handleLoginState(state: LoginState) {
    when (state) {
      is LoginState.Waiting -> binding.loading.visibility = View.GONE
      is LoginState.Loading -> binding.loading.visibility = View.VISIBLE
      is LoginState.Success -> {
        binding.loading.visibility = View.VISIBLE
        Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show()
        navigateToHome(state.userId)
      }
      is LoginState.Error -> {
        binding.loading.visibility = View.GONE
        showError(state.message)
      }
    }
  }

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
      viewModel.isFormValid.collect { isValid -> binding.login.isEnabled = isValid }
    }
  }


  private fun navigateToHome(userId: String) {
    val intent = Intent(this, HomeActivity::class.java).apply { putExtra("USER_ID", userId) } //.apply putextra("USER_ID, userId) ajout à l'intent
    startActivity(intent)
    finish()
  }


  private fun showError(message: String) {
    binding.loading.visibility = View.GONE
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }
}
