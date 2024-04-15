package com.aura.app.data.state

sealed class TransferState {
    object Waiting : TransferState()
    object Loading : TransferState()
    class Success(val message: String) : TransferState()
    class Error(val message: String) : TransferState()
}