package com.tombra.casatopia.admin_side.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.MyPropertiesAdapter
import com.tombra.casatopia._adapter.WalletAdapter
import com.tombra.casatopia.user_side.data.UserDatabase

class BACKEND: Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backend)



        val context: Context = this
        val userDatabase = UserDatabase(context)




        val propertyListAdapter = WalletAdapter()

        findViewById<RecyclerView>(R.id.recycler).apply {
            adapter = propertyListAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }


        userDatabase.getWithdrawals {

            Log.d("ACTIVITY","FINAL")
            propertyListAdapter.submitList(it)
        }







    }


    override fun onBackPressed() {
        moveTaskToBack(true)
    }



}