package com.tombra.casatopia._adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tombra.casatopia.R
import com.tombra.casatopia.databinding.ChatsBinding
import com.tombra.casatopia._model.Chat
import com.tombra.casatopia._model.User
import com.tombra.casatopia.databinding.ClientBinding

class ClientAdapter(val callback: (Int)-> Unit) :
    ListAdapter<User, ClientAdapter.ChatViewHolder>(DiffCallBack()) {

    lateinit var context: Context

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChatViewHolder {

        context = viewGroup.context
        val inflater = LayoutInflater.from(context)
        val binding = ClientBinding.inflate(inflater, viewGroup, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {




        with(holder) {
            with(getItem(position)) {


                binding.name.text = "$userFirstName $userLastName"



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

    inner class ChatViewHolder(val binding: ClientBinding) : RecyclerView.ViewHolder(binding.root){


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