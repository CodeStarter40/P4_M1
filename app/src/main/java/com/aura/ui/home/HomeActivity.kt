package com.aura.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aura.R
import com.aura.databinding.ActivityHomeBinding
import com.aura.ui.login.LoginActivity
import com.aura.ui.transfer.TransferActivity

/**
 * The home activity for the app.
 */
class HomeActivity : AppCompatActivity()
{

  /**
   * The binding for the home layout.
   */
  private lateinit var binding: ActivityHomeBinding
  private lateinit var homeViewModel: HomeViewModel

  /**
   * A callback for the result of starting the TransferActivity.
   */
  private val startTransferActivityForResult =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
      //TODO
    }

  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)

    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    //added WIP
    homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    observeAccountData()

    val userId = intent.getStringExtra("USER_ID") //pickup user by intent
    if (userId !=null){
      homeViewModel.fetchAccountUser(userId) //call method fetchAccountUser du homViewModel
    } else { //info toast if user not found
      Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show()
    }

    val balance = binding.balance
    val transfer = binding.transfer

    balance.text = "2654,54€"

    transfer.setOnClickListener {
      startTransferActivityForResult.launch(Intent(this@HomeActivity, TransferActivity::class.java))
    }
  }

  private fun observeAccountData() {
    TODO("Not yet implemented")
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean
  {
    menuInflater.inflate(R.menu.home_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean
  {
    return when (item.itemId)
    {
      R.id.disconnect ->
      {
        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
        finish()
        true
      }
      else            -> super.onOptionsItemSelected(item)
    }
  }

}
