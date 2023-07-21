package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import com.tombra.casatopia.R
import com.tombra.casatopia.user_side.activities.Profile

class MyHome : AppCompatActivity() {
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_home2)



        ///ADD


        context = this

        val fragmentManager = supportFragmentManager.findFragmentById(R.id.mainFragment) as NavHostFragment
        val navController = fragmentManager.navController


        val properties = findViewById<TextView>(R.id.properties)
        val clients = findViewById<TextView>(R.id.clients)
        val chats = findViewById<TextView>(R.id.chats)
        val transactions = findViewById<TextView>(R.id.transactions)


        var current = 1

        properties.setOnClickListener {
            when(current){
                2 -> {

                }
                3 -> {

                }
            }

            current = 1

        }

        chats.setOnClickListener {
            when(current){
                1 -> {

                }
                3 -> {

                }
            }

            current = 2

        }

        chats.setOnClickListener {

            when(current){
                1 -> {

                }
                2 -> {
                }
            }

            current = 3


        }

        transactions.setOnClickListener {
            startActivity(Intent(context, Profile::class.java))
        }


    }
}