package com.tombra.casatopia._adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tombra.casatopia.R
import com.tombra.casatopia._model.Maintenance
import com.tombra.casatopia._model.Transaction
import com.tombra.casatopia._model.Withdrawal
import com.tombra.casatopia.databinding.MaintenanceBinding
import com.tombra.casatopia.databinding.PropertiesBinding
import com.tombra.casatopia.databinding.WithdrawBinding
import com.tombra.casatopia.user_side.activities.MyProperty
import com.tombra.casatopia.user_side.activities.UserPropertyDetails
import com.tombra.casatopia.user_side.data.UserDatabase

class WalletAdapter :
    ListAdapter<Withdrawal, WalletAdapter.ChatViewHolder>(DiffCallBack()) {


    lateinit var context: Context
    lateinit var userDatabase: UserDatabase
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChatViewHolder {
        context = viewGroup.context
        userDatabase = UserDatabase(context)
        val inflater = LayoutInflater.from(context)
        val binding = WithdrawBinding.inflate(inflater, viewGroup, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {


        with(holder) {
            with(getItem(position)) {


                binding.bankName.text = "Bank name: $bankName"
                binding.accountName.text = "Account name: $accountName"
                binding.accountNumber.text = "Account number: $accountNumber"
                binding.amount.text = "Amount: ${amount.toString()}"



            }
        }
    }


    class DiffCallBack() : DiffUtil.ItemCallback<Withdrawal>() {
        override fun areItemsTheSame(oldItem: Withdrawal, newItem: Withdrawal) =
            oldItem.timestamp == newItem.timestamp

        override fun areContentsTheSame(oldItem: Withdrawal, newItem: Withdrawal) =
            oldItem == newItem
    }

    inner class ChatViewHolder(val binding: WithdrawBinding) :
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