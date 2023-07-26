package com.tombra.casatopia.admin_side.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.tombra.casatopia.R

class AdminDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)


        val profile = findViewById<TextView>(R.id.profile)

        profile.setOnClickListener {
            startActivity(Intent(this, AdminProfile::class.java))
        }

        //properties
        //clients
        //chats


        //transactions
        //notifications
        //profile



    }
}