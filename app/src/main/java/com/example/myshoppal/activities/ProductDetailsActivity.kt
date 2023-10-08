package com.example.myshoppal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.myshoppal.R
import com.example.myshoppal.databinding.ActivityProductDetailsBinding
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.CartItem
import com.example.myshoppal.model.Product
import com.example.myshoppal.utils.Constants

class ProductDetailsActivity : BaseActivity(), View.OnClickListener{
    private var binding :ActivityProductDetailsBinding?= null
    private var mProductId= ""
    private var mQuantity =""
    private lateinit var mProductDetail :Product
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product id ",mProductId)
        }
        if (intent.hasExtra(Constants.CART_QUANTITY)){
            mQuantity= intent.getStringExtra(Constants.CART_QUANTITY)!!
        }
        var productOwnerId = ""
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            productOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }
        if (FirestoresClass().getCurrentID()==productOwnerId){
            binding?.btAddToCart?.visibility =View.GONE
            binding?.btGoToCart?.visibility= View.VISIBLE
        }
        else {
            binding?.btAddToCart?.visibility = View.VISIBLE
            binding?.btGoToCart?.visibility= View.GONE
        }
        getProductDetails()
        binding?.btAddToCart?.setOnClickListener(this)
         binding?.btGoToCart?.setOnClickListener(this)
    }
    fun setupActionBar(){
        setSupportActionBar(binding?.toolbarProductDetailsActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24dp)
        binding?.toolbarProductDetailsActivity?.setNavigationOnClickListener { onBackPressed() }
    }
    fun getProductDetails(){
        FirestoresClass().getProductDetails(this,mProductId)

    }
    fun productDetailsSuccess(product:Product){
        mProductDetail= product
        Glide
            .with(this)
            .load(product.image)
            .centerCrop()
            .into(binding?.ivProductDetailImage)

        binding?.tvProductDetailsTitle?.text = product.title
        binding?.tvProductDetailsPrice?.text = "$${product.price}"
        binding?.tvProductDetailsDescription?.text = product.description
        binding?.tvProductDetailsAvailableQuantity?.text = product.quantity

    }
    fun addToCartSuccess(){
        startActivity(Intent(this,CartListActivity::class.java))
    }
    fun addToCart(){
        val cart = mProductDetail.image?.let {
            CartItem(FirestoresClass().getCurrentID(),
                mProductId,
                mProductDetail.title,
                mProductDetail.price,
                it,
                Constants.DEFAULT_CART_QUANTITY


            )
        }
        if (cart != null) {
            FirestoresClass().addToCart(this,cart)
        }
    }
    fun productExistInCart(){
        binding?.btAddToCart?.visibility = View.GONE
        binding?.btGoToCart?.visibility = View.VISIBLE
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.bt_add_to_cart->{
                addToCart()
            }
            R.id.bt_go_to_cart->{
                addToCart()
            }
        }
    }
}