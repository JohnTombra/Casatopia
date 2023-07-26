package com.tombra.casatopia.user_side.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tombra.casatopia.R
import com.tombra.casatopia.admin_side.activities.UploadProperty
import com.tombra.casatopia.user_side.data.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile)

        var adminId = intent.extras!!.getString("adminId").toString()
        var estateReferenceId = intent.extras!!.getString("estateReferenceId").toString()

        val context: Context = this

        val adminName = findViewById<TextView>(R.id.adminName)
        val adminImage = findViewById<ImageView>(R.id.adminImage)
        val startConversation = findViewById<Button>(R.id.startConversation)


        val userDatabase = UserDatabase(context)


        userDatabase.getAdminProfile(adminId){ admin ->

            //set texts stuff here

            CoroutineScope(Dispatchers.Main).launch {


                adminName.text = "${admin.firstName} ${admin.lastName}"


                Glide.with(context).load(admin.imageLink)
                    .placeholder(R.drawable.search_icon)
                    .fitCenter()
                    .centerCrop()
                    .into(adminImage)




                startConversation.setOnClickListener {
                    startActivity(
                        Intent(context, ChatWithAdmin::class.java).putExtra(
                            "adminId",
                            adminId
                        ).putExtra("estateReferenceId", estateReferenceId)
                    )

                }
            }

        }


    }
}