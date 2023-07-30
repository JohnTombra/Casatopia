package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.ClientAdapter
import com.tombra.casatopia._model.User
import com.tombra.casatopia.admin_side.data.AdminDatabase

class MyClientsActivity : AppCompatActivity() {

    private lateinit var itemClickListener: (Int) -> Unit
    lateinit var rez: List<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_clients)



        var context: Context = this
        var adminDatabase = AdminDatabase(context)


        val logo = findViewById<ImageView>(R.id.logo)
        val clientsRecycler = findViewById<RecyclerView>(R.id.clientsRecycler)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val no = findViewById<TextView>(R.id.no)


        logo.setOnClickListener {
           onBackPressed()
        }

        //my properties
        //give a complaint

        itemClickListener = { itemId ->
            val intent = Intent(context, ClientProfile::class.java)
            intent.putExtra("clientId", rez[itemId].userId)
            startActivity(intent)
        }




        val clientAdapter = ClientAdapter(itemClickListener)
        clientsRecycler.apply {
            adapter = clientAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }



        adminDatabase.getAllClients { result ->

            progress.isVisible = false

            if(result.isEmpty()){
                no.isVisible = true
            }

            rez = result
            clientAdapter.submitList(result)
        }









    }
}