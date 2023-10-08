package com.example.myshoppal.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.R
import com.example.myshoppal.activities.AddAddressActivity
import com.example.myshoppal.activities.CkeckoutActivity
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.Address
import com.example.myshoppal.utils.Constants

class ListAddressAdapter(private var context: Context,
                         private var list :ArrayList<Address>,
                         private var selectAddress:Boolean
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_address,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun notifyEditItem(activity: Activity, position: Int){
        val intent = Intent(context, AddAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS,list[position])
        activity.startActivityForResult(intent,Constants.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            holder.tvName.text= model.name
            holder.tvDetail.text = model.additionalNote
            holder.tvPhone.text = model.mobileNumber
            holder.tvType.text = model.type
//            if (selectAddress){
                holder.itemView.setOnClickListener {
                    val intent  = Intent(context,CkeckoutActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS,model)
                    context.startActivity(intent)

//                }
            }
        }

    }
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tvName :TextView = view.findViewById(R.id.tv_address_full_name)
        val tvPhone :TextView =view.findViewById(R.id.tv_address_mobile_number)
        val tvDetail :TextView = view.findViewById(R.id.tv_address_details)
        val tvType :TextView = view.findViewById(R.id.tv_address_type)
    }

}