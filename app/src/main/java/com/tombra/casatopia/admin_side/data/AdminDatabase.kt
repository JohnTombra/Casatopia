package com.tombra.casatopia.admin_side.data

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.database.FirebaseDatabase
import com.tombra.casatopia._model.Auth
import com.tombra.casatopia._model.Estate

class AdminDatabase(private val context: Context) {



//
//
    private val network: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val localSet: SharedPreferences.Editor = context.getSharedPreferences("MySharedPref",
        Context.MODE_PRIVATE
    ).edit()
    private val localGet: SharedPreferences = context.getSharedPreferences("MySharedPref",
        Context.MODE_PRIVATE
    )
//
//
//
//
    fun uploadEstate(estate: Estate, callback: ()-> Unit){
        network.reference.child("Admins").child(estate.adminId!!).child("Estates").child(estate.estateId!!).setValue(estate).addOnSuccessListener { result ->
            callback()
        }
    }

//
//
fun getAuthInfo(): Auth {
    val authenticated = localGet.getString("Authenticated", "false").toBoolean()
    val authId = localGet.getString("AuthId", "NO AUTH ID AVAILABLE").toString()
    val authType = localGet.getString("AuthType", "NO AUTH ID AVAILABLE").toString()
    return Auth(authenticated, authId, authType)
}
}