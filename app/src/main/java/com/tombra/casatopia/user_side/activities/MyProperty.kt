package com.tombra.casatopia.user_side.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tombra.casatopia.R
import com.tombra.casatopia._model.Maintenance
import com.tombra.casatopia.user_side.data.UserDatabase

class MyProperty : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_property)


        val estateId = intent.extras!!.getString("estateId").toString()
        var context: Context = this
        var userDatabase = UserDatabase(context)


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
        val garage = findViewById<TextView>(R.id.garage)
        val bathrooms = findViewById<TextView>(R.id.bathrooms)
        val bedrooms = findViewById<TextView>(R.id.bedrooms)
        val area = findViewById<TextView>(R.id.area)
        val viewFullMap = findViewById<TextView>(R.id.mappp)
        val duration = findViewById<TextView>(R.id.duration)
        val ownerShipDocument = findViewById<TextView>(R.id.ownershipDocument)
        val maintenanceRequestContainer =
            findViewById<ConstraintLayout>(R.id.maintenanceRequestContainer)
        val cancel = findViewById<TextView>(R.id.cancel)
        val requestDescription = findViewById<EditText>(R.id.requestDescription)
        val sendRequest = findViewById<TextView>(R.id.sendRequest)

        val loadingScreen = findViewById<ConstraintLayout>(R.id.loadingScreen)
        val owner = findViewById<TextView>(R.id.owner)
        val type = findViewById<TextView>(R.id.type)


        val ownerProfile = findViewById<TextView>(R.id.ownerProfile)
        val complaint = findViewById<TextView>(R.id.complaint)

        val rating = findViewById<RatingBar>(R.id.rating2)

        val logo = findViewById<ImageView>(R.id.logo)

        logo.setOnClickListener {
            onBackPressed()
        }

        Log.d("ACTIVITY", estateId)


        userDatabase.getSingleEstate(estateId) { estateFromRepository ->

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
            type.text = "Ownership type: ${estateFromRepository.type}"
            duration.text = "Duration: ${estateFromRepository.purchase!!.duration} years"


            bedrooms.text = "${estateFromRepository.bedrooms} Bedrooms"
            bathrooms.text = "${estateFromRepository.bathrooms} Bathrooms"
            area.text = "${estateFromRepository.area} SQRT"
            garage.text = "${estateFromRepository.garage} Garage"



            ownerShipDocument.setOnClickListener {
                Log.d("DOWNLOAD", "CLICKED")
                val documentLink = estateFromRepository.ownershipDocument
                retrievePdf(documentLink)
                //download document
            }

            latitude = estateFromRepository.location!!.latitude
            longitude = estateFromRepository.location!!.longitude
            onMapReady(mMap)

            rating.rating = estateFromRepository.rating!!.toFloat()


//            userDatabase.getAdminProfile(estateFromRepository.adminId!!){
//                owner.text = "${it.firstName} ${it.lastName}"
//            }

            val estateReferenceId = estateFromRepository.estateId

            ownerProfile.setOnClickListener {
                startActivity(
                    Intent(context, AdminProfile::class.java).putExtra(
                        "adminId",
                        estateFromRepository.adminId
                    ).putExtra("estateReferenceId", estateReferenceId)
                )
            }


            complaint.setOnClickListener {
                maintenanceRequestContainer.isVisible = true
            }

            cancel.setOnClickListener {
                maintenanceRequestContainer.isVisible = false
            }





            sendRequest.setOnClickListener {

                if (requestDescription.text.toString().isBlank()) {
                    Toast.makeText(context, "Request cannot be empty", Toast.LENGTH_SHORT)
                    return@setOnClickListener
                }

                //show loading layout

                loadingScreen.isVisible = true

                val maintenance = Maintenance(
                    System.currentTimeMillis().toString(),
                    requestDescription.text.toString(),
                    userDatabase.getAuthInfo().authId,
                    estateFromRepository.adminId!!,
                    estateFromRepository.estateId!!,
                    false
                )

                userDatabase.sendMaintenanceRequest(maintenance) {
                    requestDescription.setText("")
                    maintenanceRequestContainer.isVisible = false
                    loadingScreen.isVisible = false
                    Toast.makeText(context,"Sent", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(context, MaintenanceActivity::class.java))
                    //move to maintenance list activity
                }


            }





            viewFullMap.setOnClickListener {
                startActivity(
                    Intent(context, FullMap::class.java).putExtra(
                        "latitude",
                        estateFromRepository.location!!.latitude
                    ).putExtra("longitude", estateFromRepository.location!!.longitude)
                )
            }

        }

    }


    fun retrievePdf(uri: String) {
        startActivity(Intent(Intent.ACTION_VIEW).setType("application/pdf").setData(Uri.parse(uri)))
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val location = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(location).title("Property Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0F))
    }
}