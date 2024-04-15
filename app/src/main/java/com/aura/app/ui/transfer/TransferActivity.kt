package com.aura.app.ui.transfer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aura.app.data.state.TransferState
import com.aura.databinding.ActivityTransferBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TransferActivity : AppCompatActivity() {

  private lateinit var binding: ActivityTransferBinding
  private val viewModel: TransferViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityTransferBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setupObservers()

    val senderId = "1234" //temp senderId

    binding.transfer.setOnClickListener {
      val recipientId = binding.recipient.text.toString()
      val amountString = binding.amount.text.toString()

      viewModel.goTransfer(senderId, recipientId, amountString)
    }
  }

  private fun setupObservers() {
    lifecycleScope.launchWhenStarted {
      viewModel.transferState.collect { state ->
        when (state) {
          is TransferState.Loading -> binding.loading.visibility = View.VISIBLE
          is TransferState.Success -> {
            binding.loading.visibility = View.GONE
            Toast.makeText(this@TransferActivity, state.message, Toast.LENGTH_LONG).show()
            finish()
          }
          is TransferState.Error -> {
            binding.loading.visibility = View.GONE
            Toast.makeText(this@TransferActivity, state.message, Toast.LENGTH_LONG).show()
          }
          else -> binding.loading.visibility = View.GONE
        }
      }
    }
  }
}
