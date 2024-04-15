package com.aura.app.data.state

/**
 * Represents the various states of the login process.
 */
sealed class LoginState {
    object Waiting : LoginState()
    object Loading : LoginState()

    /**
     * Represents the state when an error occurs during the login process.
     * @param message The error message describing the cause of the error.
     */
    class Error(val message: String) : LoginState()

    /**
     * Représente un état de connexion réussie.
     * @param userId L'identifiant de l'utilisateur connecté.
     * Cette classe hérite de LoginState et stocke l'identifiant de l'utilisateur connecté.
     */
    class Success(val userId: String) : LoginState()
}
