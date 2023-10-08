package com.example.myshoppal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.adapter.CartListAdapter
import com.example.myshoppal.databinding.ActivityCartListBinding
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.CartItem
import com.example.myshoppal.utils.Constants

class CartListActivity : AppCompatActivity() {
    private var binding:ActivityCartListBinding? = null
    private lateinit var mCartList :ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCartListBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()
        getCartList()
        binding?.btnCheckout?.setOnClickListener {
            val intent = Intent(this,AddressActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS,true)
            startActivity(intent)
        }
    }
    fun setupActionBar(){
        setSupportActionBar(binding?.toolbarCartListActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24dp)
        binding?.toolbarCartListActivity?.setNavigationOnClickListener { onBackPressed() }
    }
    fun getCartList(){
        FirestoresClass().getCartList(this)
    }
    fun getListCartSuccess(cartList:ArrayList<CartItem>){
        if (cartList.size >0){
            mCartList =cartList
            binding?.tvNoCartItemFound?.visibility = View.GONE
            binding?.rvCartItemsList?.visibility = View.VISIBLE
            binding?.llCheckout?.visibility =View.VISIBLE
            binding?.rvCartItemsList?.layoutManager = LinearLayoutManager(this)
            binding?.rvCartItemsList?.setHasFixedSize(true)
            val adapter = CartListAdapter(this,cartList,true)
            binding?.rvCartItemsList?.adapter = adapter
            var subTotal = 0.0
            var total = 0.0
            for ( item in mCartList){
                val price =item.price.toDouble()
                val quantity = item.quantity.toInt()
                subTotal += (price*quantity)
               // total +=subTotal+10
            }
            total = subTotal +10
            binding?.tvTotalAmount?.text = "$$total"
            binding?.tvSubTotal?.text = "$${subTotal}"
            binding?.tvShippingCharge?.text= "$10"

        }
        else{
            binding?.tvNoCartItemFound?.visibility = View.VISIBLE
            binding?.rvCartItemsList?.visibility = View.GONE
            binding?.llCheckout?.visibility =View.GONE
        }
    }
    fun deleteCart(cartId:String){
        FirestoresClass().removeCart(this, cartId)

    }
    fun deleteCartSuccess(){
        getCartList()
        Toast.makeText(this,"Item remove successfully",Toast.LENGTH_SHORT).show()
    }
    fun itemUpdateSuccess(){
        getCartList()
    }
}