package com.example.findme.models

import com.google.firebase.database.Exclude
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class HomePet(
    @get:Exclude
    var id: String? = null,
    var contact: String? = null,
    var name: String? = null,
    var web: String? = null,
    var address: String? = null,
    var city: String? = null
) : Parcelable