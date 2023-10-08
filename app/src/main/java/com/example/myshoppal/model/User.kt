package com.example.myshoppal.model
import kotlinx.parcelize.Parcelize
import android.os.Parcelable


@Parcelize
data class User(
    val id :String = "",
    val firstName:String="",
    val lastName:String="",
    val email:String="",
    val image:String="",
    val mobie :Long = 0,
    val gender:String="",
    val profileComplete:Int = 0
) :Parcelable

