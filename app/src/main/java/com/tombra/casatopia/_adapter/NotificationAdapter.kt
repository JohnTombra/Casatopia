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
import com.tombra.casatopia._model.Admin
import com.tombra.casatopia._model.Chat
import com.tombra.casatopia._model.Notification
import com.tombra.casatopia.databinding.ChatItemBinding
import com.tombra.casatopia.databinding.NotificationBinding

class NotificationAdapter :
    ListAdapter<Notification, NotificationAdapter.ChatViewHolder>(DiffCallBack()) {

    lateinit var context: Context
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChatViewHolder {

        context = viewGroup.context
        val inflater = LayoutInflater.from(context)
        val binding = NotificationBinding.inflate(inflater, viewGroup, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        with(holder) {
            with(getItem(position)) {

                binding.title.text = ""
                binding.message.text = ""
                

                binding.title.text = title
                binding.message.text = message
            }
        }
    }


    class DiffCallBack() : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification) =
            oldItem.timeStamp == newItem.timeStamp

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification) =
            oldItem == newItem
    }

    inner class ChatViewHolder(val binding: NotificationBinding) : RecyclerView.ViewHolder(binding.root){


        init{
            binding.root.setOnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                }
            }
        }


    }

}