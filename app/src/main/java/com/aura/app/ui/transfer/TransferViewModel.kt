package com.aura.app.ui.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.app.data.model.Transfer
import com.aura.app.data.repository.BankRepository
import javax.inject.Inject
import com.aura.app.data.state.TransferState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@HiltViewModel
class TransferViewModel @Inject constructor( private val bankRepository: BankRepository): ViewModel() {
    private val _transferState = MutableStateFlow<TransferState>(TransferState.Waiting)
    val transferState: StateFlow<TransferState> = _transferState.asStateFlow()

    fun goTransfer(senderId: String, recipientId: String, amountString: String) {
        val amount = amountString.toDoubleOrNull()
        if (recipientId.isBlank() || amount == null || amount <= 0) {
            _transferState.value =
                TransferState.Error("Veuillez renseigner un ID de destination et un montant.")
            return
        }
        processTransfer(senderId, recipientId,amount)
    }

    private fun processTransfer(senderId:String, recipientId:String, amount:Double) {
        viewModelScope.launch {
            _transferState.value = TransferState.Loading
            try {
                val transfer = Transfer(senderId,recipientId,amount)
                val result = bankRepository.transfer(transfer)
                _transferState.value = if (result.result) {
                    TransferState.Success("Transfert Effectué")
                } else {
                    TransferState.Error ("Erreur de transfert")
                }
            } catch (e:HttpException){
                _transferState.value = TransferState.Error("Erreur HTTP ${e.code()}: ${e.message()}")
            } catch (e: IOException){
                _transferState.value = TransferState.Error("Erreur Réseau")
            } catch (e:Exception){
                _transferState.value = TransferState.Error("Erreur Inconnue")
            }
        }
    }
}