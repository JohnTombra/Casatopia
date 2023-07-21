package com.tombra.casatopia.user_side.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.ChatListAdapter
import com.tombra.casatopia._adapter.SearchItemAdapter
import com.tombra.casatopia._model.Admin
import com.tombra.casatopia._model.Estate
import com.tombra.casatopia.databinding.ActivityChatBinding
import com.tombra.casatopia.databinding.ActivityMyChatsBinding
import com.tombra.casatopia.databinding.ActivityUserHomeBinding
import com.tombra.casatopia.user_side.data.UserDatabase

class MyChats : Fragment() {

    private var _binding: ActivityMyChatsBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatItemClickListener: (Int) -> Unit

    lateinit var rez: List<Admin>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityMyChatsBinding.inflate(inflater, container, false)


        val context = requireContext()
        val userDatabase = UserDatabase(context)


        binding.logo.setOnClickListener{
            findNavController().popBackStack()
        }

        chatItemClickListener = { position ->
            val intent = Intent(context, ChatWithAdmin::class.java)
            intent.putExtra("adminId", rez[position].adminId)
            startActivity(intent)
        }

        val chatListAdapter = ChatListAdapter(chatItemClickListener)

        binding.chatsRecycler.apply {
            adapter = chatListAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }


        userDatabase.getChatList {
            rez = it
            Log.d("ACTIVITY","FINAL")
            chatListAdapter.submitList(it)

//            userDatabase.getLastMessages {
//                lastMessages = it
//            }
        }








        return binding.root
    }

}