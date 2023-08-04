package com.tombra.casatopia.admin_side.activities

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.AdminMaintenanceAdapter
import com.tombra.casatopia._adapter.MaintenanceAdapter
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia.user_side.data.UserDatabase

class MaintenanceActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)






        val context: Context = this

        val adminDatabase = AdminDatabase(context)


        val logo = findViewById<ImageView>(R.id.logo)

        logo.setOnClickListener {
            onBackPressed()
        }



        val chatListAdapter =   AdminMaintenanceAdapter()

        findViewById<RecyclerView>(R.id.clientsRecycler).apply {
            adapter = chatListAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }



        adminDatabase.getMaintenance{
            chatListAdapter.submitList(it)
            //get estate from that maintenance
        }





    }

}