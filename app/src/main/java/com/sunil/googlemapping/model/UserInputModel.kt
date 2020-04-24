package com.sunil.googlemapping.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
* data class for mapping with json file
* */
@Parcelize
data class UserInputModel(
    val userLat: Double,
    val userLon: Double,
    val accuracy: Int,
    var status : Boolean
) : Parcelable