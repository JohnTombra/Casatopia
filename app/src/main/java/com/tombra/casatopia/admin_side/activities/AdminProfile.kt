package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.tombra.casatopia.R
import com.tombra.casatopia._model.Auth
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia.user_side.activities.AUTHENTICATION_ACCOUNT_TYPE_SCREEN
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
        val userDatabase = UserDatabase(this)

        var adminId = adminDatabase.getAuthInfo().authId

        val adminName = findViewById<TextView>(R.id.adminName)
        val adminImage = findViewById<ImageView>(R.id.adminImage)

        val notification = findViewById<TextView>(R.id.notification)

        val logo = findViewById<ImageView>(R.id.logo)

        logo.setOnClickListener {
            onBackPressed()
        }




        val context: Context = this

        val add = findViewById<ImageView>(R.id.add)

        add.setOnClickListener {
            startActivity(Intent(context, UploadProperty::class.java ))
        }


        notification.setOnClickListener {
            startActivity(Intent(context, AdminNotifications::class.java ))
        }



        val logout1 = findViewById<TextView>(R.id.logout1)

        val logOutLayout = findViewById<ConstraintLayout>(R.id.logOutLayout)
        val logOut = findViewById<TextView>(R.id.logOut)
        val close = findViewById<TextView>(R.id.close)


        logout1.setOnClickListener {

            logOutLayout.isVisible = true
            logOut.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                userDatabase.saveAuthInfo(Auth("false","",""))
                startActivity(Intent(context, AUTHENTICATION_ACCOUNT_TYPE_SCREEN::class.java))
            }
            close.setOnClickListener {
               logOutLayout.isVisible = false
            }
        }

        adminDatabase.getAdminProfile{ admin ->
                adminName.text = "${admin.firstName} ${admin.lastName}"
                Glide.with(context).load(admin.imageLink)
                    .fitCenter()
                    .centerCrop()
                    .into(adminImage)
        }




    }
}