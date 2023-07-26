package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.tombra.casatopia.R
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia.user_side.activities.ChatWithAdmin
import com.tombra.casatopia.user_side.data.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile2)

        val adminDatabase = AdminDatabase(this)

        var adminId = adminDatabase.getAuthInfo().authId

        val adminName = findViewById<TextView>(R.id.adminName)
        val adminImage = findViewById<ImageView>(R.id.adminImage)

        val notification = findViewById<TextView>(R.id.notification)




        val context: Context = this

        val add = findViewById<ImageView>(R.id.add)

        add.setOnClickListener {
            startActivity(Intent(context, UploadProperty::class.java ))
        }


        notification.setOnClickListener {
            startActivity(Intent(context, AdminNotifications::class.java ))
        }


        adminDatabase.getAdminProfile{ admin ->
                adminName.text = "${admin.firstName} ${admin.lastName}"
                Glide.with(context).load(admin.imageLink)
                    .placeholder(R.drawable.search_icon)
                    .fitCenter()
                    .centerCrop()
                    .into(adminImage)
        }




    }
}