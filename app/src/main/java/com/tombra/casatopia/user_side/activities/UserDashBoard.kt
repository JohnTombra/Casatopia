package com.tombra.casatopia.user_side.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tombra.casatopia.R
import com.tombra.casatopia.user_side.data.UserDatabase

class UserDashBoard : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val context: Context = this
        val userDatabase: UserDatabase = UserDatabase(this)



        //remind theresa to install android studio in her system

        //take github link from theresa

//************//implement search for estates, houses, country, state, city


        //theresa's pov
        //Notification
        //request
        //report
        //document management
        //property overview

        /*
        * property listing
        * property detail
        * maintenance request
        * communication/notification
        * payment
        * user profile
        * admin dashboard
        * document manager
        * searach based on location like state, city, price, size
        *
        *
        * */

        //rent payment
        //all houses rended
        //rent expiry date
        //notification of rent paid, or about to expire

        //authentication

        //home page dashboard
        //user notification
        //estate search...
        //estate detail...
        //estate map...
        //chat...
        //payment...x
        //user profile...



        //sk_test_4ad704c8f3ad0e8a9c8f141b25028020371a0db9
        //pk_test_08a0aa8cb1c30848998ace5747cc8bfdee18ff16


        //document manager
        //if an estate is acquired it should remove from the general estates list. because it is owned by someone now



        //profile picture
        //name
        //view more profile info button
        //my saved estates
        //purchases

    }
}