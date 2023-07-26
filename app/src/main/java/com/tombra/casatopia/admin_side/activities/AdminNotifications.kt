package com.tombra.casatopia.admin_side.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.NotificationAdapter
import com.tombra.casatopia._adapter.SearchItemAdapter
import com.tombra.casatopia.admin_side.data.AdminDatabase

class AdminNotifications : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_notification)


        var recycler = findViewById<RecyclerView>(R.id.recycler)

        var context: Context = this
        var adminDatabase = AdminDatabase(context)


        val myAdapter = NotificationAdapter()
        recycler.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }


        adminDatabase.getNotifications { result ->
            myAdapter.submitList(result)
        }


    }
}