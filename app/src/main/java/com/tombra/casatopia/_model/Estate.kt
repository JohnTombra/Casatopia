package com.tombra.casatopia._model

data class Estate(
    val estateId: String? = null, //would be a time stamp + admin id
    val location: Location? = null,
    val estateName: String? = null,
    val country: String? = null,
    val state: String? = null,
    val city: String? = null,
    val address: String? = null,
    val propertyDescription: String? = null,
    val adminId: String? = null,
    val price: String? = null,
    val availability: Boolean? = null,
    val type: String? = null, // for rent or for sale
    val rating: Double? = 0.0,
    val image1: String =  "",
    val image2: String = "",
    val image3: String = "",
    val image4: String = "",
    val image5: String = "",
    val image6: String = "",
    val ownershipDocument: String ="",
    val bedrooms: Int = 0,
    val bathrooms: Int = 0,
    val area: String = "NOT SET",
    val garage: String = "NONE",
    val purchase: Purchase? = null

){




    data class Location(
        val latitude: Double = 0.0,
        val longitude: Double = 0.0
    )

}
