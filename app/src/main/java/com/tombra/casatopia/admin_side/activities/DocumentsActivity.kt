package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.DocumentAdapter
import com.tombra.casatopia._adapter.NotificationAdapter
import com.tombra.casatopia._model.Admin
import com.tombra.casatopia._model.Document
import com.tombra.casatopia._model.User
import com.tombra.casatopia.admin_side.data.AdminDatabase

class DocumentsActivity : AppCompatActivity() {

    private lateinit var chatItemClickListener: (Int) -> Unit

    lateinit var rez: List<Document>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documents)


        val logo = findViewById<ImageView>(R.id.logo)

        logo.setOnClickListener {
            onBackPressed()
        }


        var recycler = findViewById<RecyclerView>(R.id.recycler)

        var context: Context = this
        var adminDatabase = AdminDatabase(context)


        chatItemClickListener = { position ->
            retrievePdf(rez[position].link)
        }

        val myAdapter = DocumentAdapter(chatItemClickListener)
        recycler.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }


        adminDatabase.getDocuments { result ->
            rez = result
            myAdapter.submitList(result)
        }



    }


    fun retrievePdf(uri: String){
        startActivity(Intent(Intent.ACTION_VIEW).setType("application/pdf").setData(Uri.parse(uri)) )
    }
}