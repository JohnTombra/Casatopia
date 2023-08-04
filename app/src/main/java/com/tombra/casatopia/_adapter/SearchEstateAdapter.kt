package com.tombra.casatopia._adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tombra.casatopia.R
import com.tombra.casatopia._model.Estate
import com.tombra.casatopia.databinding.EstateItemBinding


class SearchItemAdapter(val callback: (Int) -> Unit) :
    ListAdapter<Estate, SearchItemAdapter.EstateViewHolder>(DiffCallBack()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): EstateViewHolder {

        context = viewGroup.context
        val inflater = LayoutInflater.from(context)
        val binding = EstateItemBinding.inflate(inflater, viewGroup, false)
        return  EstateViewHolder(binding)
    }


    override fun onBindViewHolder(holder: EstateViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {

                binding.image.setBackgroundColor(Color.BLACK)
                binding.name.text =""
                binding.address.text = ""
                binding.price.text = ""



                binding.name.text = estateName
                binding.address.text = address
                binding.price.text = "\u20A6 $price"
                Glide.with(context).load(image1)
                    .fitCenter()
                    .centerCrop()
                    .into(binding.image)
            }
        }
    }

    class DiffCallBack() : DiffUtil.ItemCallback<Estate>() {
        override fun areItemsTheSame(oldItem: Estate, newItem: Estate) =
            oldItem.estateId == newItem.estateId

        override fun areContentsTheSame(oldItem: Estate, newItem: Estate) =
            oldItem == newItem
    }

    inner class EstateViewHolder(val binding: EstateItemBinding) : RecyclerView.ViewHolder(binding.root){


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