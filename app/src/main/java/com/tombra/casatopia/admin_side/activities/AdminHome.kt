package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.SearchItemAdapter
import com.tombra.casatopia._model.Auth
import com.tombra.casatopia._model.Estate
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia.databinding.ActivityAdminHomeBinding
import com.tombra.casatopia.databinding.ActivityUserHomeBinding
import com.tombra.casatopia.user_side.activities.SearchEstate
import com.tombra.casatopia.user_side.activities.UserPropertyDetails
import com.tombra.casatopia.user_side.data.UserDatabase

class AdminHome : Fragment() {


    private lateinit var estateClickListener: (Int) -> Unit
    lateinit var rez: List<Estate>


    private var _binding: ActivityAdminHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ActivityAdminHomeBinding.inflate(inflater, container, false)


        var context: Context = requireContext()
        var adminDatabase = AdminDatabase(context)




        //my properties
        //give a complaint

        estateClickListener = { estateId ->
            val intent = Intent(context, AdminPropertyDetail::class.java)
            intent.putExtra("estateId", rez[estateId].estateId)
            startActivity(intent)
        }


        binding.dashboardIcon.setOnClickListener {
            startActivity(Intent(context, AdminDashboard::class.java))
        }

        val estatesAdapter = SearchItemAdapter(estateClickListener)
        binding.searchRecycler.apply {
            adapter = estatesAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }



        adminDatabase.getAllEstates { resultFromRepository ->


            binding.progress.isVisible = false

            if(resultFromRepository.isEmpty()){
                binding.no.isVisible = true
            }

            rez = resultFromRepository
            estatesAdapter.submitList(resultFromRepository)
        }




        return binding.root

 }



}