package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import com.tombra.casatopia.R
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia.user_side.activities.Profile

class MyHome : AppCompatActivity() {
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_home2)




        context = this

        val adminDatabase = AdminDatabase(context)


        val fragmentManager = supportFragmentManager.findFragmentById(R.id.mainFragment2) as NavHostFragment
        val navController = fragmentManager.navController

//
        val home = findViewById<TextView>(R.id.home)
        val clients = findViewById<TextView>(R.id.clients)
        val transactions = findViewById<TextView>(R.id.transactions)
        val chats = findViewById<TextView>(R.id.chats)


        val blue1 = findViewById<ImageView>(R.id.blue1)
        val blue2 = findViewById<ImageView>(R.id.blue2)
        val blue3 = findViewById<ImageView>(R.id.blue3)
        val blue4 = findViewById<ImageView>(R.id.blue4)





//
//
        home.setOnClickListener {
            navController.navigate(R.id.action_global_adminHome)
            blue1.isVisible = true
            blue2.isVisible = false
            blue3.isVisible = false
            blue4.isVisible = false
        }
//
//


        navController.navigate(R.id.action_global_adminHome)

        transactions.setOnClickListener {
            navController.navigate(R.id.action_global_myTransactions)
            blue4.isVisible = true
            blue1.isVisible = false
            blue3.isVisible = false
            blue2.isVisible = false
        }
//
//
        clients.setOnClickListener {
            navController.navigate(R.id.action_global_myClients)
            blue2.isVisible = true
            blue3.isVisible = false
            blue1.isVisible = false
            blue4.isVisible = false
        }
//
//
//

        chats.setOnClickListener {
            navController.navigate(R.id.action_global_myChats2)
            blue3.isVisible = true
            blue2.isVisible = false
            blue4.isVisible = false
            blue1.isVisible = false
        }



        adminDatabase.listenForAllUnread {
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