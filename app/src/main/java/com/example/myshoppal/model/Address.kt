package com.example.myshoppal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address (
    val user_id: String = "",
    val name: String = "",
    val mobileNumber :String = "",
    val address :String = "",
    val zipcode :String = "",
    val additionalNote :String  = "",
    var type:String ="",
    val otherDetail:String ="",
    var id:String = "",
    ):Parcelable
