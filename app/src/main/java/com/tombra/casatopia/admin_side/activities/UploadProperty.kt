package com.tombra.casatopia.admin_side.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tombra.casatopia.R
import com.tombra.casatopia.admin_side.data.AdminDatabase
import com.tombra.casatopia._model.Estate
import java.util.*

class UploadProperty : AppCompatActivity() {





    lateinit var context: Context


    private var mStorageref: StorageReference? = null

    private var mImageUri1: Uri? = null
    private var mImageUri2: Uri? = null
    private var mImageUri3: Uri? = null
    private var mImageUri4: Uri? = null
    private var mImageUri5: Uri? = null
    private var mImageUri6: Uri? = null

    private val PICK_IMAGE_REQUEST = 1


    var fileUri: Uri? = null;

    var selected: Int = 0

    lateinit var image1: ImageView
    lateinit var image2: ImageView
    lateinit var image3: ImageView
    lateinit var image4: ImageView
    lateinit var image5: ImageView
    lateinit var image6: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_estate)




        context = this
        val adminDatabase: AdminDatabase = AdminDatabase(this)



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


        image1 = findViewById<ImageView>(R.id.image1)
        image2 = findViewById<ImageView>(R.id.image2)
        image3 = findViewById<ImageView>(R.id.image3)
        image4 = findViewById<ImageView>(R.id.image4)
        image5 = findViewById<ImageView>(R.id.image5)
        image6 = findViewById<ImageView>(R.id.image6)



        image1.setOnClickListener {
            selected = 1
            openFileChooser()
        }

        image2.setOnClickListener {
            selected = 2
            openFileChooser()
        }

        image3.setOnClickListener {
            selected = 3
            openFileChooser()
        }

        image4.setOnClickListener {
            selected = 4
            openFileChooser()
        }

        image5.setOnClickListener {
            selected = 5
            openFileChooser()
        }

        image6.setOnClickListener {
            selected = 6
            openFileChooser()
        }

        val image1Link = ""
        val image2Link = ""
        val image3Link = ""
        val image4Link = ""
        val image5Link = ""
        val image6Link = ""

        val submit = findViewById<Button>(R.id.button)

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
                Toast.makeText(context, "Image 1 empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }




            if (mImageUri1 != null) {
                val savedas = System.currentTimeMillis().toString() + ""
                val fileReference = mStorageref!!.child(savedas)
                fileReference.putFile(mImageUri1!!)
                    .addOnSuccessListener {
                        mStorageref!!.child(savedas).downloadUrl.addOnSuccessListener { uri ->
                            val path1 = uri.toString()



                            Log.d("ACTIVITY","UPLOAD 6")

                            val estateId = System.currentTimeMillis().toString() + adminDatabase.getAuthInfo().authId
                            val adminId = adminDatabase.getAuthInfo().authId
                            val location: Estate.Location = Estate.Location()
                            val estateName = estateName.text.toString()
                            val country = country.text.toString()
                            val state = state.text.toString()
                            val city = city.text.toString()
                            val price = price.text.toString()
                            val address = address.text.toString()
                            val description = description.text.toString()
                            val type = type.text.toString()
                            val availability = true
                            val rating = 0.0


                            val estate = Estate(
                                estateId = estateId,
                                adminId = adminId,
                                location = location,
                                estateName = estateName,
                                country = country,
                                state = state,
                                city = city,
                                price = price,
                                propertyDescription = description,
                                type = type,
                                availability = availability,
                                image1 = path1,
                                rating = rating
                            )

                            Log.d("ACTIVITY","UPLOADING ESTATE")
                            adminDatabase.uploadEstate(estate){
                                //success
                                Log.d("ACTIVITY","UPLOAD SUCCESS")
                            }



                        }
                    }.addOnFailureListener {
                        Toast.makeText(context, "Upload 1 Failed", Toast.LENGTH_SHORT).show()
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
                2 -> {
                    mImageUri2 = data.data
                    Glide.with(context).load(mImageUri2).placeholder(R.drawable.avatarnoborder).centerCrop()
                        .into(image2)
                }
                3 -> {
                    mImageUri3 = data.data
                    Glide.with(context).load(mImageUri3).placeholder(R.drawable.avatarnoborder).centerCrop()
                        .into(image3)
                }
                4 -> {
                    mImageUri4 = data.data
                    Glide.with(context).load(mImageUri4).placeholder(R.drawable.avatarnoborder).centerCrop()
                        .into(image4)
                }
                5 -> {
                    mImageUri5 = data.data
                    Glide.with(context).load(mImageUri5).placeholder(R.drawable.avatarnoborder).centerCrop()
                        .into(image5)
                }
                6 -> {
                    mImageUri6 = data.data
                    Glide.with(context).load(mImageUri6).placeholder(R.drawable.avatarnoborder).centerCrop()
                        .into(image6)
                }
            }
        }
    }


    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }




    private fun uploadFile(selectedlist: String) {
        if (mImageUri1 != null && mImageUri2 != null && mImageUri3 != null && mImageUri4 != null && mImageUri5 != null && mImageUri6 != null) {
            val savedas = System.currentTimeMillis().toString() + ""
            val fileReference = mStorageref!!.child(savedas)
            fileReference.putFile(mImageUri1!!)
                .addOnSuccessListener {
                    mStorageref!!.child(savedas).downloadUrl.addOnSuccessListener { uri ->
                    val path2 = uri.toString()



                        //SAVE IMAGE AND USE SUCCESSLISTENER


                        val savedas = System.currentTimeMillis().toString() + ""
                        val fileReference = mStorageref!!.child(savedas)
                        fileReference.putFile(mImageUri1!!)
                            .addOnSuccessListener {
                                mStorageref!!.child(savedas).downloadUrl.addOnSuccessListener { uri ->
                                    val path2 = uri.toString()





                                    val savedas = System.currentTimeMillis().toString() + ""
                                    val fileReference = mStorageref!!.child(savedas)
                                    fileReference.putFile(mImageUri1!!)
                                        .addOnSuccessListener {
                                            mStorageref!!.child(savedas).downloadUrl.addOnSuccessListener { uri ->
                                                val path3 = uri.toString()




                                                val savedas = System.currentTimeMillis().toString() + ""
                                                val fileReference = mStorageref!!.child(savedas)
                                                fileReference.putFile(mImageUri1!!)
                                                    .addOnSuccessListener {
                                                        mStorageref!!.child(savedas).downloadUrl.addOnSuccessListener { uri ->
                                                            val path4 = uri.toString()


                                                            val savedas = System.currentTimeMillis().toString() + ""
                                                            val fileReference = mStorageref!!.child(savedas)
                                                            fileReference.putFile(mImageUri1!!)
                                                                .addOnSuccessListener {
                                                                    mStorageref!!.child(savedas).downloadUrl.addOnSuccessListener { uri ->
                                                                        val path5 = uri.toString()





                                                                        val savedas = System.currentTimeMillis().toString() + ""
                                                                        val fileReference = mStorageref!!.child(savedas)
                                                                        fileReference.putFile(mImageUri1!!)
                                                                            .addOnSuccessListener {
                                                                                mStorageref!!.child(savedas).downloadUrl.addOnSuccessListener { uri ->
                                                                                    val path6 = uri.toString()





                                                                                }
                                                                            }.addOnFailureListener {
                                                                                Toast.makeText(context, "Upload 1 Failed", Toast.LENGTH_SHORT).show()
                                                                            }




                                                                    }
                                                                }.addOnFailureListener {
                                                                    Toast.makeText(context, "Upload 1 Failed", Toast.LENGTH_SHORT).show()
                                                                }


                                                        }
                                                    }.addOnFailureListener {
                                                        Toast.makeText(context, "Upload 1 Failed", Toast.LENGTH_SHORT).show()
                                                    }




                                            }
                                        }.addOnFailureListener {
                                            Toast.makeText(context, "Upload 1 Failed", Toast.LENGTH_SHORT).show()
                                        }


                                }
                            }.addOnFailureListener {
                                Toast.makeText(context, "Upload 1 Failed", Toast.LENGTH_SHORT).show()
                            }





                    }
                }.addOnFailureListener {
                    Toast.makeText(context, "Upload 1 Failed", Toast.LENGTH_SHORT).show()
                }
        }else{
            Toast.makeText(context,"Fill in all images", Toast.LENGTH_SHORT).show()
        }
    }

















    fun uploadImage() {
        // on below line checking weather our file uri is null or not.
        if (fileUri != null) {

            // on below line creating a storage refrence for firebase storage and creating a child in it with
            // random uuid.
            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())
            // on below line adding a file to our storage.
            ref.putFile(fileUri!!).addOnSuccessListener {
                Toast.makeText(applicationContext, "Image Uploaded..", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Fail to Upload Image..", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }



}
