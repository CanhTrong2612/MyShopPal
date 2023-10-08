package com.example.myshoppal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.adapter.CartListAdapter
import com.example.myshoppal.databinding.ActivityMyOrderDetailsBinding
import com.example.myshoppal.model.Order
import com.example.myshoppal.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MyOrderDetailsActivity : AppCompatActivity() {
    private var binding :ActivityMyOrderDetailsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()
        var myOrderDetails: Order = Order()
        if (intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)){
            myOrderDetails = intent.getParcelableExtra(Constants.EXTRA_MY_ORDER_DETAILS)!!
        }
        setupUI(myOrderDetails)

    }
    fun setupActionBar(){
        setSupportActionBar(binding?.toolbarMyOrderDetailsActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24dp)
        binding?.toolbarMyOrderDetailsActivity?.setNavigationOnClickListener { onBackPressed() }
    }
    private fun setupUI(orderDetails: Order) {
        binding?.tvOrderDetailsId?.text = orderDetails.title
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val format = "$day:$month:$year"
        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.US)
        calendar.timeInMillis = orderDetails.order_datetime
        val orderDateTime = formatter.format(calendar.time)
        binding?.tvOrderDetailsDate?.text = orderDateTime
        val diffInMilliSeconds: Long = System.currentTimeMillis() - orderDetails.order_datetime
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        Log.e("Difference in Hours", "$diffInHours")

        when {
            diffInHours < 1 -> {
                binding?.tvOrderStatus?.text = resources.getString(R.string.order_status_pending)
                binding?.tvOrderStatus?.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.pink
                    )
                )
            }
            diffInHours < 2 -> {
                binding?.tvOrderStatus?.text  = resources.getString(R.string.order_status_in_process)
                binding?.tvOrderStatus?.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.pink
                    )
                )
            }
            else -> {
                binding?.tvOrderStatus?.text  = resources.getString(R.string.order_status_delivered)
                binding?.tvOrderStatus?.setTextColor(
                    ContextCompat.getColor(
                        this@MyOrderDetailsActivity,
                        R.color.pink
                    )
                )
            }
        }

        binding?.rvMyOrderItemsList?.layoutManager = LinearLayoutManager(this@MyOrderDetailsActivity)
        binding?.rvMyOrderItemsList?.setHasFixedSize(true)

        val cartListAdapter = CartListAdapter(this@MyOrderDetailsActivity, orderDetails.items, false)
        binding?.rvMyOrderItemsList?.adapter = cartListAdapter

        binding?.tvMyOrderDetailsAddressType?.text = orderDetails.address.type
       binding?.tvMyOrderDetailsFullName?.text = orderDetails.address.name
        binding?.tvMyOrderDetailsAddress?.text =
            "${orderDetails.address.address}, ${orderDetails.address.zipcode}"
        binding?.tvMyOrderDetailsAdditionalNote?.text = orderDetails.address.additionalNote

        if (orderDetails.address.otherDetail.isNotEmpty()) {
            binding?.tvMyOrderDetailsOtherDetails?.visibility = View.VISIBLE
            binding?.tvMyOrderDetailsOtherDetails?.text = orderDetails.address.otherDetail
        } else {
            binding?.tvMyOrderDetailsOtherDetails?.visibility = View.GONE
        }
        binding?.tvMyOrderDetailsMobileNumber?.text = orderDetails.address.mobileNumber

        binding?.tvOrderDetailsSubTotal?.text = orderDetails.sub_total_amount
        binding?.tvOrderDetailsShippingCharge?.text = orderDetails.shipping_charge
        binding?.tvOrderDetailsTotalAmount?.text = orderDetails.total_amount
    }
}