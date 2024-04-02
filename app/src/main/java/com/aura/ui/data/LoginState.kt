package com.aura.ui.data

sealed class LoginState {
    object Waiting : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    class Error(val message: String) : LoginState()
}
