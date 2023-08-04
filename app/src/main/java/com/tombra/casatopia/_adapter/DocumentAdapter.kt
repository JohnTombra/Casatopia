package com.tombra.casatopia._adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.annotations.Until
import com.tombra.casatopia.R
import com.tombra.casatopia._model.Admin
import com.tombra.casatopia._model.Chat
import com.tombra.casatopia._model.Document
import com.tombra.casatopia._model.Notification
import com.tombra.casatopia.databinding.ChatItemBinding
import com.tombra.casatopia.databinding.DocumentBinding
import com.tombra.casatopia.databinding.NotificationBinding

class DocumentAdapter(val callback: (Int) -> Unit) :
    ListAdapter<Document, DocumentAdapter.ChatViewHolder>(DiffCallBack()) {

    lateinit var context: Context
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChatViewHolder {

        context = viewGroup.context
        val inflater = LayoutInflater.from(context)
        val binding = DocumentBinding.inflate(inflater, viewGroup, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        with(holder) {
            with(getItem(position)) {

                binding.name.text = ""

                binding.name.text = "Pdf: $name"
            }
        }
    }


    class DiffCallBack() : DiffUtil.ItemCallback<Document>() {
        override fun areItemsTheSame(oldItem: Document, newItem: Document) =
            oldItem.timestamp == newItem.timestamp

        override fun areContentsTheSame(oldItem: Document, newItem: Document) =
            oldItem == newItem
    }

    inner class ChatViewHolder(val binding: DocumentBinding) : RecyclerView.ViewHolder(binding.root){


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