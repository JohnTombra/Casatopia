package com.tombra.casatopia.admin_side.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tombra.casatopia.R
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia._model.Estate
import java.util.*

class UploadProperty : AppCompatActivity(), OnMapReadyCallback {


    private var currentLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101


    lateinit var context: Context


    private var mStorageref: StorageReference? = null

    private var mImageUri1: Uri? = null
    private var mPdfUri1: Uri? = null



    private val PICK_IMAGE_REQUEST = 1
    private val PICK_PDF_REQUEST = 2

    var selected: Int = 0

    lateinit var image1: ImageView
    lateinit var imagex: ImageView
    private lateinit var ownershipDocument: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_estate)





        context = this
        val adminDatabase: AdminDatabase = AdminDatabase(this)


        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(context)
        fetchLocation()

        mStorageref =
            FirebaseStorage.getInstance("gs://casatopia-c2993.appspot.com/").getReference("images")



        val estateName = findViewById<EditText>(R.id.name)
        val country = findViewById<EditText>(R.id.country)
        val state = findViewById<EditText>(R.id.state)
        val city = findViewById<EditText>(R.id.city)
        val price = findViewById<EditText>(R.id.price)
        val description = findViewById<EditText>(R.id.description)
        val type = findViewById<EditText>(R.id.type)
        val address = findViewById<EditText>(R.id.address)



        val ratingBox = findViewById<EditText>(R.id.rating)
        val garage = findViewById<EditText>(R.id.garage)
        val bedrooms = findViewById<EditText>(R.id.bedRooms)
        val bathrooms = findViewById<EditText>(R.id.bathRooms)
        val area = findViewById<EditText>(R.id.area)

        ownershipDocument = findViewById<EditText>(R.id.ownershipDocument)


        image1 = findViewById<ImageView>(R.id.image1)
        imagex = findViewById<ImageView>(R.id.image1)


        imagex.setOnClickListener {
            selected = 1
            openFileChooser()
        }


      //  also add current location
        //owner ship document
        //rating


        val submit = findViewById<Button>(R.id.button)



        ownershipDocument.setOnClickListener {
            openPdfChooser()
        }

        submit.setOnClickListener {

            if(estateName.text.isBlank()){
                Toast.makeText(context, "Name empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if(country.text.isBlank()){
                Toast.makeText(context, "Country empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if(state.text.isBlank()){
                Toast.makeText(context, "State empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if(city.text.isBlank()){
                Toast.makeText(context, "City empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            if(mImageUri1 == null){
                Toast.makeText(context, "Image empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }





            if(bedrooms.text.isBlank()){
                Toast.makeText(context, "Bedrooms empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(bathrooms.text.isBlank()){
                Toast.makeText(context, "Bathrooms empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(garage.text.isBlank()){
                Toast.makeText(context, "Garage empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(ratingBox.text.isBlank()){
                Toast.makeText(context, "Rating empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(area.text.isBlank()){
                Toast.makeText(context, "Area empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if(currentLocation == null){
                Toast.makeText(context, "No location, enable location permission", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if(ownershipDocument.text.toString() == "Ownership document"){
                Toast.makeText(context, "No ownership document", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }





            if (mImageUri1 != null) {

                if (mPdfUri1 != null) {




                    val savedas2 = System.currentTimeMillis().toString() + ""
                    val fileReference = mStorageref!!.child(savedas2)
                    fileReference.putFile(mPdfUri1!!)
                        .addOnSuccessListener {
                            mStorageref!!.child(savedas2).downloadUrl.addOnSuccessListener { uri ->
                                val documentPath1 = uri.toString()






                                val savedas = System.currentTimeMillis().toString() + ""
                                val fileReference = mStorageref!!.child(savedas)
                                fileReference.putFile(mImageUri1!!)
                                    .addOnSuccessListener {
                                        mStorageref!!.child(savedas).downloadUrl.addOnSuccessListener { uri ->
                                            val path1 = uri.toString()



                                            Log.d("ACTIVITY", "UPLOAD 6")

                                            val estateId = System.currentTimeMillis()
                                                .toString() + adminDatabase.getAuthInfo().authId
                                            val adminId = adminDatabase.getAuthInfo().authId
                                            val location: Estate.Location = Estate.Location(
                                                currentLocation!!.latitude,
                                                currentLocation!!.longitude
                                            )
                                            val estateName = estateName.text.toString()
                                            val country = country.text.toString()
                                            val state = state.text.toString()
                                            val city = city.text.toString()
                                            val price = price.text.toString()
                                            val address = address.text.toString()
                                            val description = description.text.toString()
                                            val type = type.text.toString()
                                            val availability = true

                                            val rating = ratingBox.text.toString().toDouble()
                                            val area = area.text.toString().toInt()
                                            val bedrooms = bedrooms.text.toString().toInt()
                                            val bathrooms = bathrooms.text.toString().toInt()
                                            val garage = garage.text.toString().toInt()


                                            val estate = Estate(
                                                estateId = estateId,
                                                adminId = adminId,
                                                location = location,
                                                estateName = estateName,
                                                country = country,
                                                state = state,
                                                city = city,
                                                address = address,
                                                price = price,
                                                propertyDescription = description,
                                                type = type,
                                                availability = availability,
                                                image1 = path1,
                                                rating = rating,
                                                area = area.toString(),
                                                bedrooms = bedrooms,
                                                bathrooms = bathrooms,
                                                garage = garage.toString(),
                                                ownershipDocument = documentPath1
                                            )

                                            Log.d("ACTIVITY", "UPLOADING ESTATE")
                                            adminDatabase.uploadEstate(estate) {
                                                //success
                                                Log.d("ACTIVITY", "UPLOAD SUCCESS")
                                            }


                                        }
                                    }.addOnFailureListener {
                                        Toast.makeText(context, "Upload 1 Failed", Toast.LENGTH_SHORT).show()
                                    }



                            }
                        }.addOnFailureListener {
                            Toast.makeText(context, "Upload 2 Failed", Toast.LENGTH_SHORT).show()
                        }




            }else{
                    Toast.makeText(context,"Ownership document empty", Toast.LENGTH_SHORT).show()
            }
            }else{
                Toast.makeText(context,"Fill in all images", Toast.LENGTH_SHORT).show()
            }

        }






    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.data != null) {


            when(selected){
                1-> {
                    mImageUri1 = data.data
                    Glide.with(context).load(mImageUri1).placeholder(R.drawable.avatarnoborder).centerCrop()
                        .into(image1)
                }

            }
        }



        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.data != null) {

            mPdfUri1 = data.data
            ownershipDocument.text = mPdfUri1.toString()

        }
    }


    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }



    private fun openPdfChooser() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_PDF_REQUEST)
    }
















    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                Toast.makeText(applicationContext, currentLocation!!.latitude.toString() + "" +
                        currentLocation!!.longitude, Toast.LENGTH_SHORT).show()
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("Property here")
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19.0f))
        googleMap?.addMarker(markerOptions)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }



}
