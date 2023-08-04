package com.tombra.casatopia.user_side.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import co.paystack.android.Paystack
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.bumptech.glide.Glide
import com.tombra.casatopia.R
import com.tombra.casatopia._model.Estate
import com.tombra.casatopia._model.Purchase
import com.tombra.casatopia.user_side.data.UserDatabase


class Payment : AppCompatActivity() {

    private lateinit var userDatabase: UserDatabase
    private lateinit var estateId: String
    private var estatePrice: Int = 0
    private lateinit var context: Context
    private lateinit var estateFromRepository: Estate
    private lateinit var durationText: EditText

    private lateinit var finalize: () -> Unit


    private var totalPrice: Int = 0


    private lateinit var loadingScreen: ConstraintLayout

    private lateinit var purchaseCompleteCard: CardView


    var purchased = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        context = this
        userDatabase = UserDatabase(context)
        estateId = intent.extras!!.getString("estateId").toString()


        durationText = findViewById(R.id.duration)
        val priceText = findViewById<TextView>(R.id.propertyPrice)
        val totalPriceText = findViewById<TextView>(R.id.totalPrice)
        purchaseCompleteCard = findViewById<CardView>(R.id.purchaseCompleteCard)
        val ok = findViewById<TextView>(R.id.ok)


        val logo = findViewById<ImageView>(R.id.logo)

        logo.setOnClickListener {
            onBackPressed()
        }

        ok.setOnClickListener {
            finish()
            startActivity(Intent(context, MyPropertiesActivity::class.java))
        }

        loadingScreen = findViewById<ConstraintLayout>(R.id.loadingScreen)



        PaystackSdk.initialize(context)
        PaystackSdk.setPublicKey("pk_live_76d9f86391f1540d099acea9142c37d8cf085fbf")//set proper test or live key here

       // "pk_test_08a0aa8cb1c30848998ace5747cc8bfdee18ff16"
      //  pk_live_76d9f86391f1540d099acea9142c37d8cf085fbf
        val confirmButton = findViewById<TextView>(R.id.confirmPayment)

        val estateName = findViewById<TextView>(R.id.propertyName)
        val estateImage = findViewById<ImageView>(R.id.propertyImage)

        val propertyImage = findViewById<ImageView>(R.id.propertyImage)


        durationText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if(durationText.text.toString().isBlank()){
                    totalPriceText.text = "Total Price:  ${estatePrice.toString().replace(",","")}"
                    return
                }

                totalPrice = estatePrice * Integer.parseInt(durationText.text.toString() )

                totalPriceText.text =
                    "Total Price:  ${(estatePrice * Integer.parseInt(durationText.text.toString())).toString()}"
            }
        })

        userDatabase.getSingleEstate(estateId) { estateFromRepository ->
            totalPriceText.text = "Total Price:  ${estateFromRepository.price.toString().replace(",","")}"
            estateName.text = estateFromRepository.estateName
            estatePrice = estateFromRepository.price!!.replace(",","").toInt()
            priceText.text = "Price per year: ${estatePrice.toString()}"


            this.estateFromRepository = estateFromRepository

            confirmButton.setOnClickListener {

                if(durationText.text.toString() == "0" || durationText.text.toString().isBlank()){
                    Toast.makeText(context, "Invalid duration", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                Log.d("PAYMENT-PAGE", "Confirm button clicked - 1 ${estateFromRepository.price.toInt()}")

                loadingScreen.isVisible = true

                performCharge(totalPrice)
            }





            Glide.with(context).load(estateFromRepository.image1)
                .fitCenter()
                .centerCrop()
                .into(propertyImage)

            //set available to false


        }








    }


    private fun performCharge(amount: Int) {
        val card: Card = Card("", 0, 0, "")
        val charge = Charge()

        //set the card to charge
        charge.setCard(card)

        //call this method if you set a plan
        //charge.setPlan("PLN_yourplan");
        charge.setEmail("ekwunotessy@gmail.com") //dummy email address //set proper email address
        charge.setAmount(amount  * 100) //test amount
        PaystackSdk.chargeCard(context as Activity?, charge, object : Paystack.TransactionCallback {
            override fun onSuccess(transaction: Transaction) {

                Log.d("PAYMENT-PAGE", "Confirm button clicked - 2")





                    val purchase = Purchase(
                        if(durationText.text.toString().isBlank()){1}else{durationText.text.toString().toInt()},
                        userDatabase.getAuthInfo().authId
                    )


                    userDatabase.getUserProfile {
                        val notification = com.tombra.casatopia._model.Notification(
                            System.currentTimeMillis().toString(),
                            userDatabase.getAuthInfo().authId,
                            "Property acquired",
                            "${it.userFirstName} ${it.userLastName} has acquired your property for ${durationText.text} years"
                        )



                        val transaction = com.tombra.casatopia._model.Transaction(
                            System.currentTimeMillis().toString(),
                            estateFromRepository.estateId!!,
                        )



                        userDatabase.acquireProperty(
                            estateFromRepository.estateId!!,
                            estateFromRepository.adminId!!,
                            purchase
                        ) {
                            Log.d("PAYMENT-PAGE", "Confirm button clicked - 3")
                            //add user to clients node
                            //add to transactions

                            userDatabase.sendNotification(
                                notification,
                                estateFromRepository.adminId!!,
                                "Admins"
                            ) {
                                Log.d("PAYMENT-PAGE", "Confirm button clicked - 4")
                                //notification success

                                userDatabase.addTransactionsToAdmin(
                                    transaction,
                                    estateFromRepository.adminId!!
                                ) {
                                    Log.d("PAYMENT-PAGE", "Confirm button clicked - 5")
                                    userDatabase.addTransactionsToUser(
                                        transaction,
                                        userDatabase.getAuthInfo().authId
                                    ) {
                                        Log.d("PAYMENT-PAGE", "Confirm button clicked - 6")
                                        userDatabase.addToClientsList(
                                            estateFromRepository.adminId!!,
                                            userDatabase.getAuthInfo().authId
                                        ) {

                                            Log.d("ACTIVITY","FINALIZED")
                                            loadingScreen.isVisible = false
                                            Toast.makeText(context,"Payment successful", Toast.LENGTH_SHORT).show()
                                            purchaseCompleteCard.isVisible = true
                                            purchased = true



                                            val price = amount  - ((amount*5)/100)


                                            userDatabase.updateWallet(estateFromRepository.adminId!!, price)

                                            //save to client side

                                            //success dialog

                                        }

                                    }


                                }


                            }




                        }

                    }



            }

            override fun beforeValidate(transaction: Transaction?) {}
            override fun onError(error: Throwable?, transaction: Transaction?) {}
        })
    }


    override fun onBackPressed() {


        if(!purchased){
        super.onBackPressed()}else{
            startActivity(Intent(context, MyPropertiesActivity::class.java))
        }


    }

}