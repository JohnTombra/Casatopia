package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tombra.casatopia.R
import com.tombra.casatopia.user_side.activities.AdminProfile
import com.tombra.casatopia.user_side.activities.FullMap
import com.tombra.casatopia.user_side.activities.Payment
import com.tombra.casatopia.user_side.data.UserDatabase

class AdminTransactionDetail : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_transaction_detail)


        val estateId = intent.extras!!.getString("estateId").toString()
        var context: Context = this
        var userDatabase = UserDatabase(context)

        Log.d("FRAGMENT", "INSIDE...")

        val logo = findViewById<ImageView>(R.id.logo)

        logo.setOnClickListener {
            onBackPressed()
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map2) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //there will be a map here


        val estateImage = findViewById<ImageView>(R.id.image)
        val estateName = findViewById<TextView>(R.id.name)
        val estateAddress = findViewById<TextView>(R.id.address)
        val price = findViewById<TextView>(R.id.price)
        val country = findViewById<TextView>(R.id.country)
        val state = findViewById<TextView>(R.id.state)
        val city = findViewById<TextView>(R.id.city)
//        val garage = findViewById<TextView>(R.id.garage)
//        val bathrooms = findViewById<TextView>(R.id.bathrooms)
//        val bedrooms = findViewById<TextView>(R.id.bedrooms)
//        val area = findViewById<TextView>(R.id.area)
        val viewFullMap = findViewById<TextView>(R.id.mappp)

        val owner = findViewById<TextView>(R.id.owner)
        val type = findViewById<TextView>(R.id.type)


        val acquire = findViewById<TextView>(R.id.acquire)

        val rating = findViewById<RatingBar>(R.id.rating2)



        Log.d("ACTIVITY", estateId)


        userDatabase.getSingleEstate(estateId){ estateFromRepository ->

            val eImageLink = estateFromRepository.image1
            val eName = estateFromRepository.estateName

            Glide.with(context).load(eImageLink)

                .fitCenter()
                .centerCrop()
                .into(estateImage)

            estateName.text = eName
            estateAddress.text = estateFromRepository.address
            price.text = "\u20A6 ${estateFromRepository.price}"

            country.text = "Country: ${estateFromRepository.country}"
            state.text = "State: ${estateFromRepository.state}"
            city.text = "City: ${estateFromRepository.city}"
            type.text = "Available for: ${estateFromRepository.type}"


//            bedrooms.text = "${estateFromRepository.bedrooms} Bedrooms"
//            bathrooms.text = "${estateFromRepository.bathrooms} Bathrooms"
//            area.text = "${estateFromRepository.area} SQRT"
//            garage.text = "${estateFromRepository.garage} Garage"


            if(estateFromRepository.availability == true){
                acquire.isEnabled = false
                acquire.text = "Acquired"
                type.text = "Rented by ..."

                

            }else{
                acquire.text = "Available"

            }


            latitude = estateFromRepository.location!!.latitude
            longitude = estateFromRepository.location!!.longitude
            onMapReady(mMap)

            rating.rating = estateFromRepository.rating!!.toFloat()


//            userDatabase.getAdminProfile(estateFromRepository.adminId!!){
//                owner.text = "${it.firstName} ${it.lastName}"
//            }

            val estateReferenceId = estateFromRepository.estateId





            viewFullMap.setOnClickListener {
                startActivity(Intent(context, FullMap::class.java).putExtra("latitude",estateFromRepository.location!!.latitude).putExtra("longitude",estateFromRepository.location!!.longitude))
            }

        }


    }




    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val location = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(location).title("Property Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0F))
    }
}