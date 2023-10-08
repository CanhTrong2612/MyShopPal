package com.example.myshoppal.activities


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.R
import com.example.myshoppal.adapter.ListAddressAdapter
import com.example.myshoppal.databinding.ActivityAddressBinding
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.Address
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.SwipeToDeleteCallback
import com.example.myshoppal.utils.SwipeToEditCallback

class AddressActivity : BaseActivity() {
    private var binding :ActivityAddressBinding?= null
    private var mSelectAdress: Boolean? = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()
        binding?.tvAddAddress?.setOnClickListener {

            val intent  = Intent(this,AddAddressActivity::class.java)
            startActivityForResult(intent,Constants.ADD_ADDRESS_REQUEST_CODE)
        }
        if (intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)){
            mSelectAdress = intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS,false)
        }
        getListAddress()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            getListAddress()
        }
    }
    fun setupActionBar(){
        setSupportActionBar(binding?.toolbarAddressListActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24dp)
        binding?.toolbarAddressListActivity?.setNavigationOnClickListener { onBackPressed() }
    }
    fun getListAddressSuccess(list: ArrayList<Address>){
        if (list.size>0){
            binding?.rvAddressList?.visibility = View.VISIBLE
            binding?.tvNoAddressFound?.visibility = View.GONE
            binding?.rvAddressList?.layoutManager = LinearLayoutManager(this)
            binding?.rvAddressList?.setHasFixedSize(true)
            val adapter = ListAddressAdapter(this,list, mSelectAdress == true)
            binding?.rvAddressList?.adapter = adapter
            val editSwiperHandel = object :SwipeToEditCallback(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = binding?.rvAddressList?.adapter as ListAddressAdapter
                    adapter?.notifyEditItem(this@AddressActivity,viewHolder.adapterPosition)
                }
            }
            val editItemTouchHelper = ItemTouchHelper(editSwiperHandel)
            editItemTouchHelper.attachToRecyclerView(binding?.rvAddressList)
            ///
            val deleteSwiperHandel = object : SwipeToDeleteCallback(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    FirestoresClass().deleteAddress(this@AddressActivity,list[viewHolder.adapterPosition].id)
                }
            }
            val deleteItemTouchHelper = ItemTouchHelper(deleteSwiperHandel)
            deleteItemTouchHelper.attachToRecyclerView(binding?.rvAddressList)
        }

        else{
            binding?.rvAddressList?.visibility = View.GONE
            binding?.tvNoAddressFound?.visibility = View.VISIBLE
        }

    }

    fun getListAddress(){
        FirestoresClass().getListAddress(this)
    }


}