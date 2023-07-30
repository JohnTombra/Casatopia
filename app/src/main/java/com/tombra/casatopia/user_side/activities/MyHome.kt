package com.tombra.casatopia.user_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import com.tombra.casatopia.R
import com.tombra.casatopia.user_side.data.UserDatabase

class MyHome : AppCompatActivity() {

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_home)


        context = this

        val fragmentManager = supportFragmentManager.findFragmentById(R.id.mainFragment) as NavHostFragment
        val navController = fragmentManager.navController

        val userDatabase = UserDatabase(context)

        val home = findViewById<TextView>(R.id.home)
        val chats = findViewById<TextView>(R.id.chats)
        val properties = findViewById<TextView>(R.id.properties)
        val profile = findViewById<TextView>(R.id.profile)



        val blue1 = findViewById<ImageView>(R.id.blue1)
        val blue2 = findViewById<ImageView>(R.id.blue2)
        val blue3 = findViewById<ImageView>(R.id.blue3)
        val blue4 = findViewById<ImageView>(R.id.blue4)




        home.setOnClickListener {


            navController.navigate(R.id.action_global_userHome2)
            blue1.isVisible = true
            blue2.isVisible = false
            blue3.isVisible = false
            blue4.isVisible = false
        }


        properties.setOnClickListener {
            navController.navigate(R.id.action_global_myProperties)
            blue2.isVisible = true
            blue1.isVisible = false
            blue3.isVisible = false
            blue4.isVisible = false
        }


        chats.setOnClickListener {
            navController.navigate(R.id.action_global_myChats)
            blue3.isVisible = true
            blue2.isVisible = false
            blue1.isVisible = false
            blue4.isVisible = false
        }



        profile.setOnClickListener {
            navController.navigate(R.id.action_global_profile2)
            blue4.isVisible = true
            blue2.isVisible = false
            blue3.isVisible = false
            blue1.isVisible = false
        }


        userDatabase.listenForAllUnread {
            Log.d("HOME FRAGMENT","LISTENED FOR GLOBAL")
            if(it > 0){
                chats.text = "Chats (${it})"
            }else{
                chats.text = "Chats"
            }
        }


    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}