package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.ChatAdapter
import com.tombra.casatopia._model.Chat
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia.user_side.data.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatWithClient : AppCompatActivity() {
    private lateinit var context: Context

    var active = false

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        context = this
        val adminDatabase = AdminDatabase(context)
        val userDatabase = UserDatabase(context)
        ///initialize all views

//save to admin node also, so they can access it, transfer the estate id so it is pinned
        val chatsRecycler = findViewById<RecyclerView>(R.id.recycler)
        val send = findViewById<ImageView>(R.id.sendButton)
        val textBox = findViewById<EditText>(R.id.textBox)
        val logo = findViewById<ImageView>(R.id.logo)


        logo.setOnClickListener {
            onBackPressed()
        }


        val adminName = findViewById<TextView>(R.id.username)
        val adminImage = findViewById<ImageView>(R.id.profilePic)



        val userId = intent.extras!!.getString("clientId").toString()
        val estateReferenceId = intent.extras!!.getString("estateReferenceId").toString()





        val chatsAdapter = ChatAdapter(userDatabase.getAuthInfo().authId)
        chatsRecycler.apply {
            adapter = chatsAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }


        adminDatabase.getChats(userId){ chats ->
            chatsAdapter.submitList(chats)
        }


        adminDatabase.getClientProfile(userId){ admin ->



             CoroutineScope(Dispatchers.Main).launch {
                adminName.text = "${admin.userFirstName} ${admin.userLastName}"
                Glide.with(context).load(admin.userImageLink)
                    .fitCenter()
                    .centerCrop()
                    .into(adminImage)
            }

        }
//
//
        send.setOnClickListener {

            if(textBox.text.isBlank()){
                return@setOnClickListener
            }

            //disable the box
            send.isEnabled = false
            textBox.isEnabled = false
            send.isVisible = false

            val chat = Chat(
                System.currentTimeMillis().toString(),
                textBox.text.toString(),
                userId,
                userDatabase.getAuthInfo().authId,
                estateReferenceId
            )

            adminDatabase.sendMessage(userId, chat){
                send.isEnabled = true
                textBox.isEnabled = true
                textBox.setText("")
                send.isVisible = true
            }


        }

//
//
//



        adminDatabase.checkTotal(userId) {x,y ->
            if (active) {
                adminDatabase.updateRead(x,y)
            }
        }



    } //transactions layout should contain
    //description of service offerred and the price and a button for pay and


    override fun onStart() {
        super.onStart()
        active = true
    }




    override fun onBackPressed() {
        super.onBackPressed()
        active = false
    }



}