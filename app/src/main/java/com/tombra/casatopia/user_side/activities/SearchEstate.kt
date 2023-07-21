package com.tombra.casatopia.user_side.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tombra.casatopia.R
import com.tombra.casatopia._adapter.SearchItemAdapter
import com.tombra.casatopia._model.Auth
import com.tombra.casatopia._model.Estate
import com.tombra.casatopia.user_side.data.UserDatabase
//import com.tombra.casatopia.utility.SearchItemAdapter

class SearchEstate : AppCompatActivity() {


    private lateinit var estateClickListener: (Int) -> Unit
    lateinit var rez: List<Estate>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_estate_search_screen)
        var context: Context = this
        var userDatabase = UserDatabase(context)


        userDatabase.saveAuthInfo(Auth(true, "9", "user"))


        var searchRecycler = findViewById<RecyclerView>(R.id.searchRecycler)
        var searchEditText = findViewById<EditText>(R.id.searchEditText)



        estateClickListener = { estateId ->
            val intent = Intent(context, UserPropertyDetails::class.java)
            intent.putExtra("estateId", rez[estateId].estateId)
            startActivity(intent)
        }



        val estatesAdapter = SearchItemAdapter(estateClickListener)
        searchRecycler.apply {
            adapter = estatesAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }


        searchEditText.isEnabled = false
        userDatabase.getAllEstates { resultFromRepository ->
            rez = resultFromRepository
            searchEditText.isEnabled = true
        }


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    var query = searchEditText.text.toString().lowercase()
                    var filtered = rez.filter { it.estateName!!.lowercase().contains(query) || it.country!!.lowercase().contains(query) ||
                            it.state!!.lowercase().contains(query) || it.city!!.lowercase().contains(query) || it.address!!.lowercase().contains(query) || it.price!!.lowercase().contains(query) }
                    estatesAdapter.submitList(filtered)
                }
        })



    }
}