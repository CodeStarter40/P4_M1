package com.aura.ui.model

import android.accounts.Account

data class User(
    val id: String,
    val firstname: String,
    val lastname: String,
    val password: String,
    val accounts: List<Account>,
)