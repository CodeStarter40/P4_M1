package com.aura.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.aura.R
import com.aura.databinding.ActivityHomeBinding
import com.aura.app.ui.login.LoginActivity
import com.aura.app.ui.transfer.TransferActivity
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
  private val viewModel: HomeViewModel by viewModels()

  private lateinit var binding: ActivityHomeBinding

  private val startTransferActivityForResult =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
      //traiter le résultat de TransferActivity si nécessaire
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    observeAccountData() //call de la fonction pour observer les données du compte

    val userId = intent.getStringExtra("USER_ID") //récup de l'id du user passé en extra dans l'intent
    Log.d("HomeActivity", "Appel de fetchAccountUser avec l'ID utilisateur: $userId") //log placé pour voir si userId est null
    if (userId != null) { //verif user no null
      viewModel.fetchAccountUser(userId) //appel fetchAccountUser du viewModel
    } else {
      Log.d("FAILURE INTENT", "NO ID USER FOUND $userId")
      Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show()
    }

    //initialisation du bouton de transfert
    binding.transfer.setOnClickListener {
      startTransferActivityForResult.launch(Intent(this, TransferActivity::class.java))
    }
  }

  private fun observeAccountData() {
    lifecycleScope.launchWhenStarted {
      viewModel.accounts.collect { accounts ->
        //trouver le compte principal dans la liste
        Log.d("LOG", "OBSERVEACCOUNTDATA EXECUTED")
        val mainAccount = accounts.find { it.main }
        //mettre a jour li'nterface user avec le solde, si nul : N/A
        binding.balance.text = mainAccount?.balance?.let { "$it€" } ?: "N/A"
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.home_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.disconnect -> {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }
}
