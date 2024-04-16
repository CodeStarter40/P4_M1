package com.aura.app.ui.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.app.data.model.Transfer
import com.aura.app.data.repository.BankRepository
import com.aura.app.data.state.TransferState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject




@HiltViewModel
class TransferViewModel @Inject constructor( private val bankRepository: BankRepository): ViewModel() {
    private val _transferState = MutableStateFlow<TransferState>(TransferState.Waiting)
    val transferState: StateFlow<TransferState> = _transferState.asStateFlow()

    /**
     * @goTransfer is a function to check the following conditions:
     * - Recipient field is not empty
     * - Amount is not null
     * - Amount is not zero and not negative
     * If the conditions are met, the function @processTransfer is called.
     */

    fun goTransfer(senderId: String, recipientId: String, amountString: String) {
        val amount = amountString.toDoubleOrNull()
        if (recipientId.isBlank() || amount == null || amount <= 0) {
            _transferState.value =
                TransferState.Error("Veuillez renseigner un ID de destination et un montant.")
            return
        }
        processTransfer(senderId, recipientId,amount)
    }

    /**
     * Processes the transfer with the provided sender ID, recipient ID, and amount.
     * @param senderId The ID of the sender.
     * @param recipientId The ID of the recipient.
     * @param amount The amount to transfer.
     */
    private fun processTransfer(senderId:String, recipientId:String, amount:Double) {
        viewModelScope.launch {
            _transferState.value = TransferState.Loading
            try {
                //initiates the transfer / initialisation du transfert
                val transfer = Transfer(senderId,recipientId,amount)
                val result = bankRepository.transfer(transfer)
                //updates transfer state based on the result / MAJ de l'état en fonction du resultat
                _transferState.value = if (result.result) {
                    TransferState.Success("Transfer Effectué")
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