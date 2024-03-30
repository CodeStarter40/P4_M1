package com.aura.ui.data

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    class Error(val message: String) : LoginState()
}
