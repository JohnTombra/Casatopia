package com.tombra.casatopia._adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tombra.casatopia.R
import com.tombra.casatopia._model.Maintenance
import com.tombra.casatopia._model.Transaction
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia.databinding.AdminMaintenanceBinding
import com.tombra.casatopia.databinding.MaintenanceBinding
import com.tombra.casatopia.databinding.PropertiesBinding
import com.tombra.casatopia.user_side.activities.MyProperty
import com.tombra.casatopia.user_side.activities.UserPropertyDetails
import com.tombra.casatopia.user_side.data.UserDatabase

class AdminMaintenanceAdapter :
    ListAdapter<Maintenance, AdminMaintenanceAdapter.ChatViewHolder>(DiffCallBack()) {


    lateinit var context: Context
    lateinit var adminDatabase: AdminDatabase
    lateinit var userDatabase: UserDatabase
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChatViewHolder {
        context = viewGroup.context
        userDatabase = UserDatabase(context)
        adminDatabase = AdminDatabase(context)
        val inflater = LayoutInflater.from(context)
        val binding = AdminMaintenanceBinding.inflate(inflater, viewGroup, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {


        with(holder) {
            with(getItem(position)) {

                binding.image.setBackgroundColor(Color.BLACK)
                binding.message.text = ""




                userDatabase.getSingleEstate(estate) { estate ->
                    binding.acknowledged.text = if (acknowledged) {
                        binding.acknowledged.isEnabled = false
                        binding.acknowledged.setBackgroundColor(Color.rgb(75, 175, 80))
                        "Acknowledged"

                    } else {
                   //     binding.acknowledged.setBackgroundColor(Color.LTGRAY)

                        binding.acknowledged.setOnClickListener {
                            binding.acknowledged.text = "Loading..."


                            adminDatabase.acknowledge("Users/$sender/Maintenance/$timestamp/acknowledged","Admins/$receiver/Maintenance/$timestamp/acknowledged"){

                            }
                        }


                        "Acknowledge"


                    }
                    binding.message.text = message
                    Glide.with(context).load(estate.image1)
                        .fitCenter()
                        .centerCrop()
                        .into(binding.image)

                }


            }
        }
    }


    class DiffCallBack() : DiffUtil.ItemCallback<Maintenance>() {
        override fun areItemsTheSame(oldItem: Maintenance, newItem: Maintenance) =
            oldItem.timestamp == newItem.timestamp

        override fun areContentsTheSame(oldItem: Maintenance, newItem: Maintenance) =
            oldItem == newItem
    }

    inner class ChatViewHolder(val binding: AdminMaintenanceBinding) :
        RecyclerView.ViewHolder(binding.root) {


        init {
            binding.root.setOnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {


                    //set on click liestener here

                }
            }
        }


    }

}