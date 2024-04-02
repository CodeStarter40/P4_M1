package com.aura.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.ui.api.LoginService
import com.aura.ui.model.Account
import com.aura.ui.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception


class HomeViewModel(private val loginService: LoginService): ViewModel() {
    //use stateFlow for publish result
    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()

    fun fetchAccountUser(userId: String) {
        viewModelScope.launch {
            try {
                val accountsList = loginService.getAccountsByUserId(userId)
                _accounts.value = accountsList
            } catch (e: Exception) {
                //TODO add http exeption
            }
        }
    }
}