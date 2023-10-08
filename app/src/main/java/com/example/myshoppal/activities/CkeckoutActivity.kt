package com.example.myshoppal.activities

import android.content.Intent
import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.DashBoardFragment
import com.example.myshoppal.R
import com.example.myshoppal.adapter.CartListAdapter
import com.example.myshoppal.databinding.ActivityCkeckoutBinding
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.CartItem
import com.example.myshoppal.model.Order
import com.example.myshoppal.model.Product
import com.example.myshoppal.utils.Constants

class CkeckoutActivity : BaseActivity() {
    private var binding: ActivityCkeckoutBinding? = null
    private var mAddressDetails: com.example.myshoppal.model.Address? = null
    private var mProductList :ArrayList<Product>? = null
    private lateinit var mCartItemList :ArrayList<CartItem>
    private var mSubTotal = 0.0
    private var mtotal = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCkeckoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()
        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)
        }
        if (mAddressDetails!=null){
            binding?.tvCheckoutAddressType?.text = mAddressDetails?.type
            binding?.tvCheckoutFullName?.text = mAddressDetails?.name
            binding?.tvCheckoutAddress?.text = "${mAddressDetails!!.address},${mAddressDetails!!.zipcode}"
            binding?.tvCheckoutAdditionalNote?.text = mAddressDetails?.additionalNote
            if (mAddressDetails!!.otherDetail!!.isNotEmpty()){
                binding?.tvCheckoutOtherDetails?.text = mAddressDetails?.otherDetail
            }
            binding?.tvCheckoutMobileNumber?.text = mAddressDetails?.mobileNumber
        }
        binding?.btnPlaceOrder?.setOnClickListener { placeAnOrder() }
        getProductList()
    }
    fun getAllProductSuccess(list:ArrayList<Product>){
        mProductList = list
        FirestoresClass().getCartList(this)
    }
    fun getProductList(){
        FirestoresClass().getAllProduct(this)
    }
    fun successCartItemList(cartList:ArrayList<CartItem>){
//        for(product in mProductList!!){
//            for (cartItem in cartList){
//                cartItem.quantity = product.quantity
//                if (product.quantity.toInt()==0){
//                    cartItem.cart_quantity = product.quantity
//                }
//            }
//        }
        mCartItemList= cartList
        if (mCartItemList.size>0){
            binding?.rvCartListItems?.visibility = View.VISIBLE
            binding?.rvCartListItems?.layoutManager = LinearLayoutManager(this)
            binding?.rvCartListItems?.setHasFixedSize(true)
            val adapter = CartListAdapter(this,mCartItemList,false)
            binding?.rvCartListItems?.adapter = adapter
            for (item in mCartItemList){
                val price =item.price.toDouble()
                val quantity = item.quantity.toInt()
                mSubTotal += (price*quantity)
            }
            mtotal = mSubTotal+10
            binding?.tvCheckoutSubTotal?.text = mSubTotal.toString()
            binding?.tvCheckoutTotalAmount?.text = mtotal.toString()

        }
    }

    fun setupActionBar(){
        setSupportActionBar(binding?.toolbarCheckoutActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24dp)
        binding?.toolbarCheckoutActivity?.setNavigationOnClickListener { onBackPressed() }
    }
    fun orderPlaceSuccess(){
        hideProgressDialog()
        Toast.makeText(this,"Your order placed successfully",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
    fun placeAnOrder(){
         showProgressDialog()
        if (mAddressDetails!=null) {
            val order = Order(
                FirestoresClass().getCurrentID(),
                mCartItemList,
                mAddressDetails!!,
                "My Order ${System.currentTimeMillis()}",
                mCartItemList[0].image,
                mSubTotal.toString(),
                "10.0",
                mtotal.toString(),
            )
            FirestoresClass().placeAnorder(this,order)
        }

    }
}