package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.tombra.casatopia.R
import com.tombra.casatopia._model.Auth
import com.tombra.casatopia.user_side.activities.AUTHENTICATION_ACCOUNT_TYPE_SCREEN
import com.tombra.casatopia.user_side.data.UserDatabase

class AdminDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val logo = findViewById<ImageView>(R.id.logo)

        logo.setOnClickListener {
            onBackPressed()
        }

        val context: Context = this

        val userDatabase = UserDatabase(this)

        val profile = findViewById<TextView>(R.id.profile)
        val add = findViewById<TextView>(R.id.add)
        val documents = findViewById<TextView>(R.id.documents)
        val myEstates = findViewById<TextView>(R.id.myEstates)
        val chats = findViewById<TextView>(R.id.chats)
        val notifications = findViewById<TextView>(R.id.notifications)
        val clients = findViewById<TextView>(R.id.clients)
        val logout = findViewById<TextView>(R.id.logout)

        val logOutLayout = findViewById<ConstraintLayout>(R.id.logOutLayout)
        val logOut = findViewById<TextView>(R.id.logOut)
        val close = findViewById<TextView>(R.id.close)




        profile.setOnClickListener {
            startActivity(Intent(this, AdminProfile::class.java))
        }

        add.setOnClickListener {
            startActivity(Intent(this, UploadProperty::class.java))
        }

        documents.setOnClickListener {
            startActivity(Intent(this, DocumentsActivity::class.java))
        }

        myEstates.setOnClickListener {
            startActivity(Intent(this, MyEstatesActivity::class.java))
            //create an activity that shows estates
        }


        chats.setOnClickListener {
            startActivity(Intent(this, MyChatsActivity::class.java))
            //create an activity that shows chats
        }


        notifications.setOnClickListener {
            startActivity(Intent(this, AdminNotifications::class.java))
            //create an activity that shows chats
        }


        logout.setOnClickListener {


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


        clients.setOnClickListener {
            startActivity(Intent(this, MyClientsActivity::class.java))
        }










        //properties
        //clients
        //chats


        //transactions
        //notifications
        //profile



    }
}