package com.tombra.casatopia.user_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.cardview.widget.CardView
import com.google.firebase.database.FirebaseDatabase
import com.tombra.casatopia.R
import com.tombra.casatopia._model.Admin
import com.tombra.casatopia._model.Auth
import com.tombra.casatopia._model.Signup
import com.tombra.casatopia._model.User
import com.tombra.casatopia.user_side.data.UserDatabase

class AUTHENTICATION_ACCOUNT_TYPE_SCREEN : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication_account_type_screen)


        //check is local is set

        val context: Context = this

        val userDatabase = UserDatabase(context)

       val accountType = userDatabase.getAuthInfo().accountType
        if(accountType.isBlank()){

            val user = findViewById<CardView>(R.id.userCard)
            val admin = findViewById<CardView>(R.id.adminCard)
            val network: FirebaseDatabase = FirebaseDatabase.getInstance()

            user.setOnClickListener {

                //get the
                network.reference.child("Accounts").child(userDatabase.getAuthInfo().authId).get().addOnSuccessListener {
                    val result = it.getValue(Signup::class.java)

                    val user = User(
                        userDatabase.getAuthInfo().authId,
                        result!!.firstName!!,
                        result!!.lastName!!,
                        "",
                        "Users"
                    )





                    network.reference.child("Users").child(userDatabase.getAuthInfo().authId).child("Profile").setValue(user).addOnSuccessListener {
                        val userDatabase = UserDatabase(context)
                        val auth = Auth(
                            authenticated = "true",
                            authId = userDatabase.getAuthInfo().authId,
                            accountType = "Users"
                        )
                        userDatabase.saveAuthInfo(auth)



                        val signUp =  Signup(
                            userDatabase.getAuthInfo().authId,
                            true,
                            "Users",
                            result.firstName,
                            result.lastName
                        )

                        network.reference.child("Accounts").child(userDatabase.getAuthInfo().authId).setValue(signUp).addOnSuccessListener {
                            startActivity(Intent(context,MyHome::class.java))
                        }
                        //also update accounts

                        //go to admin home
                    }

                }



            }

            admin.setOnClickListener {


                network.reference.child("Accounts").child(userDatabase.getAuthInfo().authId).get().addOnSuccessListener {
                    val result = it.getValue(Signup::class.java)

                    val admin = Admin(
                        userDatabase.getAuthInfo().authId,
                        result!!.firstName!!,
                        result!!.lastName!!,
                        "",
                        "Admins"
                    )

                    network.reference.child("Admins").child(userDatabase.getAuthInfo().authId).child("Profile").setValue(admin).addOnSuccessListener {
                        val userDatabase = UserDatabase(context)
                        val auth = Auth(
                            authenticated = "true",
                            authId = userDatabase.getAuthInfo().authId,
                            accountType = "Admins"
                        )
                        userDatabase.saveAuthInfo(auth)




                        val signUp =  Signup(
                            userDatabase.getAuthInfo().authId,
                            true,
                            "Admins",
                        result.firstName,
                            result.lastName
                        )

                        network.reference.child("Accounts").child(userDatabase.getAuthInfo().authId).setValue(signUp).addOnSuccessListener {
                            startActivity(Intent(context,com.tombra.casatopia.admin_side.activities.MyHome::class.java))}
                        //go to admin home
                    }
                }


            }

        }else{


            if(accountType == "Admins"){
                startActivity(Intent(context,com.tombra.casatopia.admin_side.activities.MyHome::class.java ))
            }
            if(accountType == "Users"){
                startActivity(Intent(context,com.tombra.casatopia.user_side.activities.MyHome::class.java ))
            }
        }



    }


    override fun onStart() {

        super.onStart()

        val context: Context = this
        val mAuth = UserDatabase(context)

        Log.d("SIGN UP", mAuth.getAuthInfo().authenticated)
        if(mAuth.getAuthInfo().authenticated == "false"){
            startActivity(Intent(context, AUTHENTICATION_LOGIN::class.java))
        }else{
        }

    }
}