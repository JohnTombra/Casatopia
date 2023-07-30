package com.tombra.casatopia.admin_side.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.ChatListAdapter
import com.tombra.casatopia._adapter.ChatListAdminAdapter
import com.tombra.casatopia._adapter.SearchItemAdapter
import com.tombra.casatopia._model.Admin
import com.tombra.casatopia._model.Estate
import com.tombra.casatopia._model.User
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia.databinding.ActivityChatBinding
import com.tombra.casatopia.databinding.ActivityMyChatsBinding
import com.tombra.casatopia.databinding.ActivityUserHomeBinding
import com.tombra.casatopia.databinding.AdminMychatsBinding
import com.tombra.casatopia.user_side.data.UserDatabase

class MyChats : Fragment() {

    private var _binding: AdminMychatsBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatItemClickListener: (Int) -> Unit

    lateinit var rez: List<User>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AdminMychatsBinding.inflate(inflater, container, false)


        val context = requireContext()
        val adminDatabase = AdminDatabase(context)


        binding.logo.setOnClickListener{
            findNavController().popBackStack()
        }

        chatItemClickListener = { position ->
            val intent = Intent(context, ChatWithClient::class.java)
            intent.putExtra("clientId", rez[position].userId)
            startActivity(intent)
        }

        val chatListAdapter = ChatListAdminAdapter(chatItemClickListener)

        binding.chatsRecycler.apply {
            adapter = chatListAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }


        adminDatabase.getChatList {
            binding.progress.isVisible = false

            if(it.isEmpty()){
                binding.no.isVisible = true
            }


            rez = it
            Log.d("ACTIVITY","FINAL")
            chatListAdapter.submitList(it)
        }








        return binding.root
    }

}