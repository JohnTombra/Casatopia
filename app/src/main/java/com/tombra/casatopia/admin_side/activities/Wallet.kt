package com.tombra.casatopia.admin_side.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.tombra.casatopia.R
import com.tombra.casatopia._model.Withdrawal
import com.tombra.casatopia.admin_side.data.AdminDatabase

class Wallet : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_wallet)

        val context: Context = this
        val adminDatabase = AdminDatabase(context)

        val amount = findViewById<EditText>(R.id.amount)

        val bankName = findViewById<EditText>(R.id.bankName)
        val accountName = findViewById<EditText>(R.id.accountName)
        val accountNumber = findViewById<EditText>(R.id.accountNumber)
        val withdraw = findViewById<Button>(R.id.withdraw)
        val currentBalance = findViewById<TextView>(R.id.currentBalance)

        val loadingScreen = findViewById<ConstraintLayout>(R.id.loadingScreen)


        findViewById<ImageView>(R.id.logo).setOnClickListener{
            onBackPressed()
        }

        //listen for balance here

        adminDatabase.listenForBalance(adminDatabase.getAuthInfo().authId){balance ->
            currentBalance.text = "Wallet balance: ₦${balance}"
        }


        withdraw.setOnClickListener {

            loadingScreen.isVisible = true
            //show loading box

            val withdrawal = Withdrawal(
                System.currentTimeMillis().toString(),
                amount.text.toString().toInt(),
                bankName.text.toString(),
                accountName.text.toString(),
                accountNumber.text.toString()
            )

            adminDatabase.makeWithdrawal(adminDatabase.getAuthInfo().authId, amount.text.toString().toInt(), withdrawal){

                //hide loading box

                loadingScreen.isVisible = false
                Toast.makeText(context, "Withdrawal successful", Toast.LENGTH_SHORT).show()

            }
        }



    }


}