package com.aura.ui.data

/**
 * Represents the various states of the login process.
 */
sealed class LoginState {
    object Waiting : LoginState()
    object Loading : LoginState()
    object Success : LoginState()

    /**
     * Represents the state when an error occurs during the login process.
     * @param message The error message describing the cause of the error.
     */
    class Error(val message: String) : LoginState()
}
