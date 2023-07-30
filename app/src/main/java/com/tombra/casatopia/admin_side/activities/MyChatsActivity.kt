package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.ChatListAdminAdapter
import com.tombra.casatopia._model.User
import com.tombra.casatopia.admin_side.data.AdminDatabase

class MyChatsActivity : AppCompatActivity() {


    private lateinit var chatItemClickListener: (Int) -> Unit

    lateinit var rez: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_chats2)





        val context: Context = this
        val adminDatabase = AdminDatabase(context)


        val logo = findViewById<ImageView>(R.id.logo)
        val chatsRecycler = findViewById<RecyclerView>(R.id.chatsRecycler)
        val progress = findViewById<ProgressBar>(R.id.progress)

        logo.setOnClickListener{
            onBackPressed()
        }

        chatItemClickListener = { position ->
            val intent = Intent(context, ChatWithClient::class.java)
            intent.putExtra("clientId", rez[position].userId)
            startActivity(intent)
        }

        val chatListAdapter = ChatListAdminAdapter(chatItemClickListener)

        chatsRecycler.apply {
            adapter = chatListAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }


        adminDatabase.getChatList {
            progress.isVisible = false
            rez = it
            Log.d("ACTIVITY","FINAL")
            chatListAdapter.submitList(it)
        }






    }
}