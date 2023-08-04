package com.tombra.casatopia.user_side.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tombra.casatopia.R
import com.tombra.casatopia._model.Signup
import com.tombra.casatopia.user_side.data.UserDatabase

class AUTHENTICATION_SIGNUP : AppCompatActivity() {


    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    private lateinit var mAuth: FirebaseAuth

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication_welcome_screen)


        context = this

        mAuth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val verifyPassword = findViewById<EditText>(R.id.confirmPassword)
        val firstName = findViewById<EditText>(R.id.firstName)
        val lastName = findViewById<EditText>(R.id.lastName)
        val signup = findViewById<TextView>(R.id.signup)
        val phoneNumber = findViewById<EditText>(R.id.phoneNumber)

        val gotoLogin = findViewById<TextView>(R.id.login)

        val loadingScreen = findViewById<ConstraintLayout>(R.id.loadingScreen)



        gotoLogin.setOnClickListener {
            startActivity(Intent(context, AUTHENTICATION_LOGIN::class.java))
        }


        signup.setOnClickListener {

            if (TextUtils.isEmpty(email.text.toString())) {
                Toast.makeText(context, "Fill in your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password.text.toString())) {
                Toast.makeText(context, "Fill in your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(verifyPassword.text.toString())) {
                Toast.makeText(context, "Confirm your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.text.toString() != verifyPassword.text.toString()) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(firstName.text.toString())) {
                Toast.makeText(context, "Fill in your first name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (TextUtils.isEmpty(lastName.text.toString())) {
                Toast.makeText(context, "Fill in your last name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loadingScreen.isVisible = true

            //add check for if email already exists
            mAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        loadingScreen.isVisible = false
                        Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT)
                            .show()

                        val network: FirebaseDatabase = FirebaseDatabase.getInstance()

                        val signup = Signup(
                            mAuth.currentUser!!.uid,
                            false,
                            "",
                            firstName.text.toString(),
                            lastName.text.toString(),
                            email.text.toString(),
                            phoneNumber.text.toString()
                        )

                        network.reference.child("Accounts").child(mAuth.currentUser!!.uid)
                            .setValue(signup).addOnSuccessListener {
                            startActivity(Intent(context, AUTHENTICATION_LOGIN::class.java))
                        }

                    } else {
                        loadingScreen.isVisible = false
                        Toast.makeText(
                            context,
                            "Registration failed ${task.exception!!.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }


        }


    }


    override fun onBackPressed() {
        moveTaskToBack(true)
    }


}