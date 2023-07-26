package com.tombra.casatopia.user_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.SearchItemAdapter
import com.tombra.casatopia._model.Auth
import com.tombra.casatopia._model.Estate
import com.tombra.casatopia.databinding.ActivityUserHomeBinding
import com.tombra.casatopia.user_side.data.UserDatabase

class UserHome : Fragment() {

    private lateinit var estateClickListener: (Int) -> Unit
    lateinit var rez: List<Estate>


    private var _binding: ActivityUserHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityUserHomeBinding.inflate(inflater, container, false)


        var context: Context = requireContext()
        var userDatabase = UserDatabase(context)


     //   userDatabase.saveAuthInfo(Auth(true, "9", "user"))




        //my properties
        //give a complaint

        estateClickListener = { estateId ->
            val intent = Intent(context, UserPropertyDetails::class.java)
            intent.putExtra("estateId", rez[estateId].estateId)
            startActivity(intent)
        }


        binding.searchIcon.setOnClickListener {
            val intent = Intent(context, SearchEstate::class.java)
            startActivity(intent)
        }

        val estatesAdapter = SearchItemAdapter(estateClickListener)
        binding.searchRecycler.apply {
            adapter = estatesAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }



        userDatabase.getAllEstates { resultFromRepository ->
            rez = resultFromRepository
            estatesAdapter.submitList(resultFromRepository)
        }




        return binding.root
    }


}