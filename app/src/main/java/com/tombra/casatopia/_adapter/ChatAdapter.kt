package com.tombra.casatopia._adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tombra.casatopia.databinding.ChatsBinding
import com.tombra.casatopia._model.Chat

class ChatAdapter(val userId: String) :
    ListAdapter<Chat, ChatAdapter.ChatViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChatViewHolder {

        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ChatsBinding.inflate(inflater, viewGroup, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {




        with(holder) {
            with(getItem(position)) {


                val text = message

                if(sender == userId){
                    binding.textRightCard.isVisible = true
                    binding.textRight.text = text
                }else{
                    binding.textLeftCard.isVisible = true
                    binding.textLeft.text = text
                }

            }
        }
    }


    class DiffCallBack() : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat) =
            oldItem.timestamp == newItem.timestamp

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat) =
            oldItem == newItem
    }

    inner class ChatViewHolder(val binding: ChatsBinding) : RecyclerView.ViewHolder(binding.root){


        init{
            binding.root.setOnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION){

                }
            }
        }


    }

}