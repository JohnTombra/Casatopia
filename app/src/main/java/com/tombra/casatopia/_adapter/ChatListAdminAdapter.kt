package com.tombra.casatopia._adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tombra.casatopia.R
import com.tombra.casatopia._model.Admin
import com.tombra.casatopia._model.Chat
import com.tombra.casatopia._model.User
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia.databinding.ChatItemBinding

class ChatListAdminAdapter(val callback: (Int)->Unit) :
    ListAdapter<User, ChatListAdminAdapter.ChatViewHolder>(DiffCallBack()) {

    lateinit var context: Context
    lateinit var adminDatabase: AdminDatabase
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChatViewHolder {

        context = viewGroup.context
        adminDatabase = AdminDatabase(context)
        val inflater = LayoutInflater.from(context)
        val binding = ChatItemBinding.inflate(inflater, viewGroup, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        with(holder) {
            with(getItem(position)) {


                binding.bubbleCard.isVisible = false
                binding.bubbleText.text = ""
                binding.name.text = ""
                binding.image.setBackgroundColor(Color.BLACK)


                adminDatabase.listenForSingleUnread(userId) {data1, data2 ->
                    if (data2 > 0) {
                        binding.bubbleCard.isVisible = true
                        binding.bubbleText.text = (data2).toString()

                    } else {
                        binding.bubbleCard.isVisible = false
                        binding.bubbleText.text = ""
                    }
                }


                binding.name.text = "${userFirstName!!} ${userLastName!!}"

                Glide.with(context).load(userImageLink)
                    .fitCenter()
                    .centerCrop()
                    .into(binding.image)

            }
        }
    }


    class DiffCallBack() : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) =
            oldItem.userId == newItem.userId

        override fun areContentsTheSame(oldItem: User, newItem: User) =
            oldItem == newItem
    }

    inner class ChatViewHolder(val binding: ChatItemBinding) : RecyclerView.ViewHolder(binding.root){


        init{
            binding.root.setOnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    callback(position)
                }
            }
        }


    }

}