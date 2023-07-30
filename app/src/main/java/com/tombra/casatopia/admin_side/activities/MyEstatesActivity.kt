package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.SearchItemAdapter
import com.tombra.casatopia._model.Estate
import com.tombra.casatopia.admin_side.data.AdminDatabase

class MyEstatesActivity : AppCompatActivity() {

    private lateinit var estateClickListener: (Int) -> Unit
    lateinit var rez: List<Estate>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_estates)


        var context: Context = this
        var adminDatabase = AdminDatabase(context)


        val logo = findViewById<ImageView>(R.id.logo)

        logo.setOnClickListener {
            onBackPressed()
        }

        val searchRecycler = findViewById<RecyclerView>(R.id.recycler)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val no = findViewById<TextView>(R.id.no)


        estateClickListener = { estateId ->
            val intent = Intent(context, AdminPropertyDetail::class.java)
            intent.putExtra("estateId", rez[estateId].estateId)
            startActivity(intent)
        }


        val estatesAdapter = SearchItemAdapter(estateClickListener)
        searchRecycler.apply {
            adapter = estatesAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }



        adminDatabase.getAllEstates { resultFromRepository ->


            progress.isVisible = false

            if(resultFromRepository.isEmpty()){
               no.isVisible = true
            }

            rez = resultFromRepository
            estatesAdapter.submitList(resultFromRepository)
        }






    }
}