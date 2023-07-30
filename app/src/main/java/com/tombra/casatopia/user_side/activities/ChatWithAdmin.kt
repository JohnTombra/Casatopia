package com.tombra.casatopia.user_side.activities

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
import com.tombra.casatopia.user_side.data.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatWithAdmin : AppCompatActivity() {
    private lateinit var context: Context
    var active = false

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        context = this
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



        val adminId = intent.extras!!.getString("adminId").toString()
        val estateReferenceId = intent.extras!!.getString("estateReferenceId").toString()





        val chatsAdapter = ChatAdapter(userDatabase.getAuthInfo().authId)
        chatsRecycler.apply {
            adapter = chatsAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }





        userDatabase.getChats(adminId){ chats ->
            chatsAdapter.submitList(chats)
        }

        userDatabase.getAdminProfile(adminId){ admin ->



             CoroutineScope(Dispatchers.Main).launch {
                adminName.text = "${admin.firstName} ${admin.lastName}"
                Glide.with(context).load(admin.imageLink)
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
                adminId,
                userDatabase.getAuthInfo().authId,
                estateReferenceId
            )

            userDatabase.sendMessage(adminId, chat){
                send.isEnabled = true
                textBox.isEnabled = true
                textBox.setText("")
                send.isVisible = true
            }
        }




        userDatabase.checkTotal(adminId) {x,y ->
            if (active) {
                userDatabase.updateRead(x,y)
            }
        }




//
//
//


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


    //if page is active set read as same if total changes


}