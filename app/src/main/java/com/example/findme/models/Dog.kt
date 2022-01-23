package com.example.findme.models

import com.google.firebase.database.Exclude
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Dog(
    @get:Exclude
    var id: String? = null,
    var image_url: String? = null,
    var type: String? = null,
    var pet_home: String? = null,
    var gender: String? = null,
    var found: String? = null,
    var description: String? = null,
    var date: String? = null,
    var address: String? = null,
    var pet_home_link: String? = null,
    var city: String? = null
) : Parcelable

//    private var image_url: String = ""
//    private var type: String = ""
//
//    constructor()
//    constructor(image_url: String, type: String) {
//        this.image_url = image_url
//        this.type = type
//    }
//
//    fun getType(): String{
//        return type
//    }
//
//    fun getImageUrl(): String{
//        return image_url
//    }
