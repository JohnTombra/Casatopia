package com.tombra.casatopia.user_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.MyPropertiesAdapter
import com.tombra.casatopia._model.Transaction
import com.tombra.casatopia.databinding.ActivityMyPropertiesBinding
import com.tombra.casatopia.user_side.data.UserDatabase

class MyPropertiesActivity : AppCompatActivity() {



    private lateinit var propertyItemClickListener: (Int) -> Unit

    lateinit var rez: List<Transaction>

    lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_properties2)



        context = this
        val userDatabase = UserDatabase(context)




        findViewById<ImageView>(R.id.logo).setOnClickListener{
            finish()
            startActivity(Intent(context, MyHome::class.java))
        }


        val propertyListAdapter = MyPropertiesAdapter()

        findViewById<RecyclerView>(R.id.propertiesRecycler).apply {
            adapter = propertyListAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }


        userDatabase.getPropertyList {

            findViewById<ProgressBar>(R.id.progress).isVisible = false

            if(it.isEmpty()){
                findViewById<TextView>(R.id.no).isVisible = true
            }

            rez = it
            Log.d("ACTIVITY","FINAL")
            propertyListAdapter.submitList(it)
        }







    }


    override fun onBackPressed() {
        super.onBackPressed()

        finish()
        startActivity(Intent(context, MyHome::class.java))
    }

}