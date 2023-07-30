package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.ClientAdapter
import com.tombra.casatopia._model.User
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia.databinding.FragmentMyclientsBinding

class MyClients: Fragment() {


    private lateinit var itemClickListener: (Int) -> Unit
    lateinit var rez: List<User>


    private var _binding: FragmentMyclientsBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyclientsBinding.inflate(inflater, container, false)





        var context: Context = requireContext()
        var adminDatabase = AdminDatabase(context)


        val logo = binding.logo


        logo.setOnClickListener {
            findNavController().popBackStack()
        }

        //my properties
        //give a complaint

        itemClickListener = { itemId ->
            val intent = Intent(context, ClientProfile::class.java)
            intent.putExtra("clientId", rez[itemId].userId)
            startActivity(intent)
        }




        val clientAdapter = ClientAdapter(itemClickListener)
        binding.clientsRecycler.apply {
            adapter = clientAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }



        adminDatabase.getAllClients { result ->

            binding.progress.isVisible = false

            if(result.isEmpty()){
                binding.no.isVisible = true
            }

            rez = result
            clientAdapter.submitList(result)
        }










        return binding.root
    }



}