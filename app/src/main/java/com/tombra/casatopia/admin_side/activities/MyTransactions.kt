package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.SearchItemAdapter
import com.tombra.casatopia._adapter.TransactionAdapter
import com.tombra.casatopia._model.Estate
import com.tombra.casatopia._model.Transaction
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia.databinding.FragmentTransactionsBinding

class MyTransactions : Fragment() {


    private lateinit var itemClickListener: (Int) -> Unit
    lateinit var rez: List<Transaction>


    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)



        var context: Context = requireContext()
        var adminDatabase = AdminDatabase(context)



        val logo = binding.logo

        logo.setOnClickListener {
            findNavController().popBackStack()
        }

        //my properties
        //give a complaint

        itemClickListener = { estateId ->

//            Log.d("FRAGMENT", "CLICKED")
//            val intent = Intent(context, AdminPropertyDetail::class.java)
//            intent.putExtra("estateId", rez[estateId].estateId)
//            startActivity(intent)
        }
//


        val transactionAdapter = TransactionAdapter(itemClickListener)
        binding.transactionsRecycler.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }






        adminDatabase.getTransactionList { result ->

            binding.progress.isVisible = false

            if(result.isEmpty()){
                binding.no.isVisible = true
            }

            rez = result
            transactionAdapter.submitList(result)
        }





        return binding.root
    }


}