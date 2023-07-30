package com.tombra.casatopia.user_side.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.ChatListAdapter
import com.tombra.casatopia._adapter.MyPropertiesAdapter
import com.tombra.casatopia._model.Admin
import com.tombra.casatopia._model.Estate
import com.tombra.casatopia._model.Transaction
import com.tombra.casatopia.databinding.ActivityChatBinding
import com.tombra.casatopia.databinding.ActivityMyChatsBinding
import com.tombra.casatopia.databinding.ActivityMyPropertiesBinding
import com.tombra.casatopia.user_side.data.UserDatabase

class MyProperties : Fragment() {


    private var _binding: ActivityMyPropertiesBinding? = null
    private val binding get() = _binding!!

    private lateinit var propertyItemClickListener: (Int) -> Unit

    lateinit var rez: List<Transaction>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityMyPropertiesBinding.inflate(inflater, container, false)


        val context = requireContext()
        val userDatabase = UserDatabase(context)



        binding.logo.setOnClickListener{
            findNavController().popBackStack()
        }


        val propertyListAdapter = MyPropertiesAdapter()

        binding.propertiesRecycler.apply {
            adapter = propertyListAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }


        userDatabase.getPropertyList {

            binding.progress.isVisible = false

            if(it.isEmpty()){
                binding.no.isVisible = true
            }

            rez = it
            Log.d("ACTIVITY","FINAL")
            propertyListAdapter.submitList(it)
        }







        return binding.root
    }
}