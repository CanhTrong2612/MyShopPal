package com.example.myshoppal.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.myshoppal.activities.ProfileActivity

object Constants {
    const val PRODUCTS ="products"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val MYSHOPPAL_PREFERENCES: String = "MyShopPalPrefs"
    const val EXTRA_USER_DETAILS :String  = "extra_user_details"
    const val MALE ="male"
    const val FERMALE= "fermale"
    const val MOBIE = "mobie"
    const val GENDER ="gender"
    const val IMAGE ="image"
    const val USER_ID ="user_id"
  //  const val USERS_ID ="user_Id"
    const val COMPLETE_PROFILE ="profileComplete"
    const val USER_PROFILE_IMAGE = "user_profile_image"
    const val EXTRA_PRODUCT_ID ="extra_product_id"
    const val EXTRA_PRODUCT_OWNER_ID ="extra_product_owner_id"
    const val DEFAULT_CART_QUANTITY = "1"
    const val CART_ITEMS ="cart_items"
    const val PRODUCT_ID ="product_id"
    const val CART_QUANTITY="quantity"
    const val HOME:String = "home"
    const val OFFICE :String ="ofice"
    const val OTHER :String ="other"
    const val ADDRESSES : String = "Addresses"
    const val EXTRA_ADDRESS_DETAILS = "AddressDetails"
    const val EXTRA_SELECT_ADDRESS = "extra_select_address"
    const val EXTRA_SELECTED_ADDRESS = "extra_selected_address"
    const val ADD_ADDRESS_REQUEST_CODE = 121
    const val ORDERS ="orders"
    const val EXTRA_MY_ORDER_DETAILS:String = "extra_my_order_details"
    fun getFileExtension(activity: Activity,uri: Uri):String{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
            .toString()
    }

}