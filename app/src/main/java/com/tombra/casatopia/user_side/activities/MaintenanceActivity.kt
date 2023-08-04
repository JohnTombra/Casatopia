package com.tombra.casatopia.user_side.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.ChatListAdapter
import com.tombra.casatopia._adapter.MaintenanceAdapter
import com.tombra.casatopia.user_side.data.UserDatabase

class MaintenanceActivity: Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)


        val context: Context = this

        val userDatabase = UserDatabase(context)


        val logo = findViewById<ImageView>(R.id.logo)

        logo.setOnClickListener {
            onBackPressed()
        }



        val chatListAdapter = MaintenanceAdapter()

        findViewById<RecyclerView>(R.id.clientsRecycler).apply {
            adapter = chatListAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }



        userDatabase.getMaintenance{
            chatListAdapter.submitList(it)
            //get estate from that maintenance
        }




    }


}