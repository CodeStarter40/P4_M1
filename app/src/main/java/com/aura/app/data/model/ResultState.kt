package com.aura.app.data.model

/**
 * Represents the various states of the login process.
 */
sealed class ResultState {
    object Waiting : ResultState()
    object Loading : ResultState()

    /**
     * Represents the state when an error occurs during the login process.
     * @param message The error message describing the cause of the error.
     */
    class Error(val message: String) : ResultState()

    /**
     * Représente un état de connexion réussie.
     * @param userId L'identifiant de l'utilisateur connecté.
     * Cette classe hérite de ResultState et stocke l'identifiant de l'utilisateur connecté.
     */
    class Success(val userId: String) : ResultState()
}
