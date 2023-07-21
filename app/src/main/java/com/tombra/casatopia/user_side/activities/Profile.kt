package com.tombra.casatopia.user_side.activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tombra.casatopia.R
import com.tombra.casatopia.databinding.ActivityProfileBinding
import com.tombra.casatopia.databinding.ActivityUserHomeBinding
import com.tombra.casatopia.user_side.data.UserDatabase

class Profile : Fragment() {


    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ActivityProfileBinding.inflate(inflater, container, false)


        val context: Context = requireContext()
        val userDatabase = UserDatabase(context)

        binding.logo.setOnClickListener{
            findNavController().popBackStack()
        }

        //see the total number of properties, see the total number of chats, see and access ntifications


        val userName = binding.name
        val userImage = binding.image

        userDatabase.getUserProfile{ userFromRepository ->


            Glide.with(context).load(userFromRepository.userImageLink)
                .placeholder(R.drawable.search_icon)
                .fitCenter()
                .centerCrop()
                .into(userImage)

            userName.text =  "${userFromRepository.userFirstName} ${userFromRepository.userLastName}"







        }





        return binding.root
    }


}