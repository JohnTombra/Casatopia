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
import com.tombra.casatopia.databinding.ChatItemBinding

class ChatListAdapter(val callback: (Int)->Unit) :
    ListAdapter<Admin, ChatListAdapter.ChatViewHolder>(DiffCallBack()) {

    lateinit var context: Context
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChatViewHolder {

        context = viewGroup.context
        val inflater = LayoutInflater.from(context)
        val binding = ChatItemBinding.inflate(inflater, viewGroup, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        with(holder) {
            with(getItem(position)) {

                binding.name.text = "${firstName!!} ${lastName!!}"

                Glide.with(context).load(imageLink)
                    .placeholder(R.drawable.search_icon)
                    .fitCenter()
                    .centerCrop()
                    .into(binding.image)

            }
        }
    }


    class DiffCallBack() : DiffUtil.ItemCallback<Admin>() {
        override fun areItemsTheSame(oldItem: Admin, newItem: Admin) =
            oldItem.adminId == newItem.adminId

        override fun areContentsTheSame(oldItem: Admin, newItem: Admin) =
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