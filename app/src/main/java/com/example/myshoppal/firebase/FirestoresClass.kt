package com.example.myshoppal.firebase

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.myshoppal.DashBoardFragment
import com.example.myshoppal.OrderFragment
import com.example.myshoppal.ProductFragment
import com.example.myshoppal.activities.*
import com.example.myshoppal.adapter.CartListAdapter
import com.example.myshoppal.model.*
import com.example.myshoppal.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoresClass {
    private var mFireStore = FirebaseFirestore.getInstance()
    fun registerUser(activity:RegisterActivity, user:User){
        mFireStore.collection("users")
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisterSuccess()
            }
            .addOnFailureListener {e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while registering the user",e)

            }
    }
    fun uploadProductDetails(activity:AddProductActivity,product: Product){
        mFireStore.collection(Constants.PRODUCTS)
            .document()
            .set(product, SetOptions.merge())
            .addOnSuccessListener {
               activity.productUploadSuccess()
            }
            .addOnFailureListener {e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while uploading the product",e)

            }
    }
//    fun getCurrentID():String{
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        var currentUserId = ""
//        if (currentUserId!= null){
//            currentUserId = currentUser!!.uid
//        }
//        return currentUserId
//    }
fun getCurrentID(): String {
    // An Instance of currentUser using FirebaseAuth
    val currentUser = FirebaseAuth.getInstance().currentUser

    // A variable to assign the currentUserId if it is not null or else it will be blank.
    var currentUserID = ""
    if (currentUser != null) {
        currentUserID = currentUser.uid
    }

    return currentUserID
}
    fun getUserDetail(activity: Activity){
        mFireStore.collection("users")
            .document(getCurrentID())
            .get()
            .addOnSuccessListener {document->
                val user = document.toObject(User::class.java)
                when(activity){
                     is LoginActivity->{
                         if (user!= null)
                             activity. userLoggedInSuccess(user)
                         }
                     is ProfileActivity ->{
                         if (user!= null)
                            activity.getDataUser(user)
                     }
                    is SettingActivity ->{
                        if (user!= null)
                            activity.userDetailsSuccess(user)
                    }
                    }
                }
            }
    fun updateUserProfile(activity: Activity,userHashMap: HashMap<String,Any>){
        mFireStore.collection("users")
            .document(getCurrentID())
            .update(userHashMap)
            .addOnSuccessListener { e->
                when(activity){
                    is ProfileActivity->
                    {
                        activity.updateProfileSuccess()
                    }
                }
            }
            .addOnFailureListener {e->
                Log.e(activity.javaClass.simpleName,"Error while updating the user details",e)
            }
    }
    fun uploadImageToCloudStorge(activity: Activity,imageUri: Uri?) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() +
                    "." + Constants.getFileExtension(activity, imageUri!!)
        )
        sRef.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
            Log.e("Firebase image url", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
            taskSnapshot.metadata!!.reference!!.downloadUrl
        }
            .addOnSuccessListener { uri ->
                Log.e("Downlaod image Url", uri.toString())
                when (activity) {
                    is ProfileActivity -> {
                        activity.imageUploadSuccess(imageUri!!.toString())
                    }
                    is AddProductActivity -> {
                        activity.imageUploadSuccess(imageUri!!.toString())
                    }
                }

            }
            .addOnFailureListener {
                when (activity) {
                    is ProfileActivity ->
                        activity.hideProgressDialog()
                }
            }
    }
    fun getProductDetails(activity: ProductDetailsActivity, productID: String){
        mFireStore.collection(Constants.PRODUCTS)
            .document(productID)
            .get()
            .addOnSuccessListener { document->
                val product = document.toObject(Product::class.java)
                Log.e("Products List",product.toString())
                if (product!= null)
                  activity.productDetailsSuccess(product)
            }
            .addOnFailureListener {e->
                Log.e(activity.javaClass.simpleName,"Error while getting the product details",e)
            }
    }
    fun getAllProduct(activity: CkeckoutActivity){
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener {document->
                Log.e("Product list: ",document.documents.toString())
                val productList = ArrayList<Product>()
                for (i in document.documents){
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productList.add(product)
                }
                activity.getAllProductSuccess(productList)
            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName,"Error while get All product",e)
            }
    }
    fun getProductsList(fragment: Fragment){
        mFireStore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID,getCurrentID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productList:ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)!!
                    product.product_id = i.id
                    productList.add(product)
                }
                    when (fragment) {
                        is ProductFragment ->{
                            fragment.successProductList(productList)
                        }
                    }

                }

            .addOnFailureListener {
                when(fragment){
                    is ProductFragment->
                        fragment.hideProgressDialog()
                }
            }
    }
     fun getDashboardList(fragment:DashBoardFragment){
         mFireStore.collection(Constants.PRODUCTS)
             .get()
             .addOnSuccessListener { document ->
                 Log.e("Products List", document.documents.toString())
                 val productList:ArrayList<Product> = ArrayList()
                 for (i in document.documents) {
                     val product = i.toObject(Product::class.java)
                     product!!.product_id= i.id
                     productList.add(product)
                 }
//                 when(fragment){
//                     is DashBoardFragment ->
//                     fragment.successDashboardItemList(productList)
//                 }
             }
             .addOnFailureListener {
                 when(fragment){
                     is DashBoardFragment->
                         fragment.hideProgressDialog()
                 }
             }
     }
    fun deleteProduct(fragment: ProductFragment,productID:String){
        mFireStore.collection(Constants.PRODUCTS)
            .document(productID)
            .delete()
            .addOnSuccessListener {
                fragment.productDeleteSuccess()
            }
            .addOnFailureListener { e->
                fragment.hideProgressDialog()
                Log.e(fragment.requireActivity().javaClass.simpleName,"Error while deleting the product")
            }
    }
    fun addToCart(activity:ProductDetailsActivity,cart:CartItem){
        mFireStore.collection(Constants.CART_ITEMS)
            .document()
            .set(cart, SetOptions.merge())
            .addOnSuccessListener {
              activity.addToCartSuccess()
            }
            .addOnFailureListener {  }

    }
    fun checkCart(activity:ProductDetailsActivity,productID: String){
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.PRODUCT_ID,productID)
            .whereEqualTo(Constants.USER_ID,getCurrentID())
            .get()
            .addOnSuccessListener {document->
                if (document.documents.size>0){
                    activity.productExistInCart()
                }

            }
            .addOnFailureListener {
                activity.hideProgressDialog()
            }
    }

fun getCartList(activity: Activity) {
    mFireStore.collection(Constants.CART_ITEMS)
        .whereEqualTo(Constants.USER_ID, getCurrentID())
        .get()
        .addOnSuccessListener { document ->
            Log.e(activity.javaClass.simpleName, document.documents.toString())
            val list: ArrayList<CartItem> = ArrayList()
            for (i in document.documents) {
                val cartItem = i.toObject(CartItem::class.java)!!
                cartItem.id = i.id
                list.add(cartItem)
            }

            when (activity) {
                is CartListActivity -> {
                    activity.getListCartSuccess(list)
                }
                is CkeckoutActivity ->{
                    activity.successCartItemList(list)
                }
            }
        }
        .addOnFailureListener { e ->
            when (activity) {
                is CartListActivity -> {
                }
            }
            Log.e(activity.javaClass.simpleName, "Error while getting the cart list items.", e)
         }
    }
    fun removeCart(activity: CartListActivity,cartId:String){
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cartId)
            .delete()
            .addOnSuccessListener {
                activity.deleteCartSuccess()
            }
            .addOnFailureListener {e->
                Log.e(activity.javaClass.simpleName,"Error while remove the cart list")
            }

    }
    fun updateMyCart(context: Context, cartId:String, itemHashMap:HashMap<String,Any>){
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cartId)
            .update(itemHashMap)
            .addOnSuccessListener {
                when(context){
                    is CartListActivity ->{
                        context.itemUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e->
                Log.e(context.javaClass.simpleName,"Error while update cart")
            }

    }
    fun addAddress(activity: AddAddressActivity,address:Address){
        mFireStore.collection(Constants.ADDRESSES)
            .document()
            .set(address, SetOptions.merge())
            .addOnSuccessListener {
                activity.addAddressSuccess()
            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName,"Error while add the address",e)
            }
    }
    fun getListAddress(activity: AddressActivity){
        mFireStore.collection(Constants.ADDRESSES)
            .whereEqualTo(Constants.USER_ID,getCurrentID())
            .get()
            .addOnSuccessListener { document->
                Log.e("listAddress",document.documents.toString())
                val listAddress = ArrayList<Address>()
                for ( i in document.documents){
                    val address = i.toObject(Address::class.java)!!
                    address.id = i.id
                    listAddress.add(address)
                }
                activity.getListAddressSuccess(listAddress)
            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName,"error while get the address",e)
            }
    }
    fun updateAddress(activity: AddAddressActivity,address:Address,id:String){
        mFireStore.collection(Constants.ADDRESSES)
            .document()
            .set(address, SetOptions.merge())
            .addOnSuccessListener {
                activity.addAddressSuccess()
            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName,"error update the address",e)
            }
    }
    fun deleteAddress(activity: AddressActivity,id:String){
        mFireStore.collection(Constants.ADDRESSES)
            .document(id)
            .delete()
            .addOnSuccessListener {
                activity.getListAddress()
            }
    }
    fun placeAnorder(activity: CkeckoutActivity,order:Order){
        mFireStore.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.orderPlaceSuccess()
            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName,"error",e)
            }

    }
    fun getMyOrderList(fragment: OrderFragment){
        mFireStore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID,getCurrentID())
            .get()
            .addOnSuccessListener { document->
                Log.e("order list ", document.documents.toString())
                val list:ArrayList<Order> = ArrayList()
                for (i in document.documents){
                    val orderItem = i.toObject(Order::class.java)!!
                    orderItem.id = i.id
                    list.add(orderItem)
                }
                fragment.populateListOrder(list)

            }
            .addOnFailureListener { e->
                Log.e(fragment.javaClass.simpleName,"Error while getting the order list",e)
            }
    }
    }
