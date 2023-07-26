package com.tombra.casatopia.user_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tombra.casatopia.R
import com.tombra.casatopia._model.Admin
import com.tombra.casatopia._model.Auth
import com.tombra.casatopia._model.Signup
import com.tombra.casatopia._model.User
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia.user_side.data.UserDatabase

class AUTHENTICATION_LOGIN : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication_verify_screen)


        val context: Context = this

        mAuth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<TextView>(R.id.login)
        val gotoSignup = findViewById<TextView>(R.id.signup)

        val network: FirebaseDatabase = FirebaseDatabase.getInstance()

        gotoSignup.setOnClickListener {
            startActivity(Intent(context, AUTHENTICATION_SIGNUP::class.java))
        }



        login.setOnClickListener {
            if (TextUtils.isEmpty(email.text.toString())) {
                Toast.makeText(context, "Fill in your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password.text.toString())) {
                Toast.makeText(context, "Fill in your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }




            val userDatabase = UserDatabase(context)



            mAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener { task ->


                    if (task.isSuccessful) {
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        //get user data from database and save into shared preference




                        network.reference.child("Accounts").child(mAuth.currentUser!!.uid).get().addOnSuccessListener {

                            val result = it.getValue(Signup::class.java)!!

                            Log.d("AUTH", result.type!!)
                            if(result.type!! == ""){
                                Log.d("AUTH", "${result.type} -----1")
                                val userDatabase = UserDatabase(context)
                                val auth = Auth(
                                    authenticated = "true",
                                    authId = mAuth.currentUser!!.uid,
                                    accountType = ""
                                )
                                userDatabase.saveAuthInfo(auth)

                                startActivity(
                                    Intent(
                                        context,
                                        AUTHENTICATION_ACCOUNT_TYPE_SCREEN::class.java
                                    )
                                )

                            }else{
                                Log.d("AUTH", "${result.type} -----2")
                                if(result!!.type == "Admins"){
                                    val admin = Admin(
                                        mAuth.currentUser!!.uid,
                                        result!!.firstName!!,
                                        result!!.lastName!!,
                                        "",
                                        "Admins"
                                    )
                                    Log.d("AUTH", "${result.type} -----3")
                                    network.reference.child("Admins").child(mAuth.currentUser!!.uid)
                                        .child("Profile").setValue(admin).addOnSuccessListener {

                                            val userDatabase = UserDatabase(context)
                                            val auth = Auth(
                                                authenticated = "true",
                                                authId = mAuth.currentUser!!.uid,
                                                accountType = "Admins"
                                            )
                                            userDatabase.saveAuthInfo(auth)

                                            startActivity(
                                                Intent(
                                                    context,
                                                    com.tombra.casatopia.admin_side.activities.MyHome::class.java
                                                )
                                            )
                                            //go to admin home
                                        }


                                }


                                if(result!!.type == "Users"){
                                    val user = User(
                                        mAuth.currentUser!!.uid,
                                        result!!.firstName!!,
                                        result!!.lastName!!,
                                        "",
                                        "Users"
                                    )
                                    Log.d("AUTH", "${result.type} -----4")
                                    network.reference.child("Users").child(mAuth.currentUser!!.uid)
                                        .child("Profile").setValue(user).addOnSuccessListener {
                                            val userDatabase = UserDatabase(context)
                                            val auth = Auth(
                                                authenticated = "true",
                                                authId = mAuth.currentUser!!.uid,
                                                accountType = "Users"
                                            )
                                            userDatabase.saveAuthInfo(auth)

                                            startActivity(
                                                Intent(
                                                    context,
                                                    com.tombra.casatopia.user_side.activities.MyHome::class.java
                                                )
                                            )
                                            //go to admin home
                                        }

                                }


                            }


                        }




                    } else {
                        Toast.makeText(
                            context,
                            "Login failed ${task.exception!!.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }


        }


    }
}