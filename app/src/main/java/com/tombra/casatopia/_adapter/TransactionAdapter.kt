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
import com.tombra.casatopia._model.Transaction
import com.tombra.casatopia.admin_side.activities.AdminPropertyDetail
import com.tombra.casatopia.admin_side.activities.AdminTransactionDetail
import com.tombra.casatopia.databinding.PropertiesBinding
import com.tombra.casatopia.user_side.activities.MyProperty
import com.tombra.casatopia.user_side.activities.UserPropertyDetails
import com.tombra.casatopia.user_side.data.UserDatabase

class TransactionAdapter(val callback: (Int)-> Unit) :
    ListAdapter<Transaction, TransactionAdapter.ChatViewHolder>(DiffCallBack()) {



    lateinit var context: Context
    lateinit var userDatabase: UserDatabase
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChatViewHolder {
        context = viewGroup.context
        userDatabase = UserDatabase(context)
        val inflater = LayoutInflater.from(context)
        val binding = PropertiesBinding.inflate(inflater, viewGroup, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {




        with(holder) {
            with(getItem(position)) {

                binding.image.setBackgroundColor(Color.BLACK)
                binding.name.text =""
                binding.location.text = ""
                binding.amountPaid.text = ""
                binding.duration.text = ""
                binding.layout.setOnClickListener {  }


                userDatabase.getSingleEstate(estateId){ estate ->

                    binding.name.text = estate.estateName


                    binding.duration.text = "Duration: ${estate.purchase!!.duration} years"

                    binding.amountPaid.text = "Payment: ₦ ${estate.price!!.replace("₦","").replace(",","").toInt() * estate.purchase!!.duration.toInt()}"

                    binding.location.text = "${estate.country}, ${estate.state}, ${estate.city}"

                    binding.layout.setOnClickListener {
                        context.startActivity(Intent(context, AdminPropertyDetail::class.java).putExtra("estateId",estate.estateId))
                    }

                    Glide.with(context).load(estate.image1)
                        .fitCenter()
                        .centerCrop()
                        .into(binding.image)

                }
            }
        }
    }


    class DiffCallBack() : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) =
            oldItem.transactionId == newItem.transactionId

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) =
            oldItem == newItem
    }

    inner class ChatViewHolder(val binding: PropertiesBinding) : RecyclerView.ViewHolder(binding.root){


        init{
            binding.root.setOnClickListener {
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION){


               //     callback(position)

                    //set on click liestener here

                }
            }
        }


    }

}