package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tombra.casatopia.R
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia.user_side.activities.ChatWithAdmin
import com.tombra.casatopia.user_side.data.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClientProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_profile)



        val context: Context = this
        val adminDatabase = AdminDatabase(context)

        val clientId: String = intent.extras!!.getString("clientId").toString()

        val logo = findViewById<ImageView>(R.id.logo)


        logo.setOnClickListener {
            onBackPressed()
        }

        //see the total number of properties, see the total number of chats, see and access ntifications


        val userName = findViewById<TextView>(R.id.name)
        val userImage = findViewById<ImageView>(R.id.image)
        val chat = findViewById<TextView>(R.id.startConversation)


        adminDatabase.getClientProfile(clientId){ userFromRepository ->

            CoroutineScope(Dispatchers.Main).launch {


                Glide.with(context).load(userFromRepository.userImageLink)
                    .fitCenter()
                    .centerCrop()
                    .into(userImage)

                userName.text =
                    "${userFromRepository.userFirstName} ${userFromRepository.userLastName}"

            }


            chat.setOnClickListener {
                startActivity(
                    Intent(context, ChatWithClient::class.java).putExtra(
                        "clientId",
                        userFromRepository.userId
                    ).putExtra("estateReferenceId", "")
                )
            }

        }







    }
}