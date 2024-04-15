package com.aura.app.ui.transfer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aura.app.data.state.TransferState
import com.aura.databinding.ActivityTransferBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope


/**
 * The transfer activity for the app.
 */
@AndroidEntryPoint
class TransferActivity : AppCompatActivity() {

  /**
   * The binding for the transfer layout.
   */
  private lateinit var binding: ActivityTransferBinding
  private val viewModel: TransferViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityTransferBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setupObservers()

    val recipient = binding.recipient
    val amount = binding.amount
    val transfer = binding.transfer
    val loading = binding.loading

    binding.transfer.setOnClickListener {
      loading.visibility = View.VISIBLE
      val recipientId = recipient.text.toString()
      val amountString = amount.text.toString()

      loading.visibility = View.VISIBLE

      viewModel.goTransfer(recipientId, amountString)
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
