package com.example.myshoppal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myshoppal.R
import com.example.myshoppal.activities.MyOrderDetailsActivity
import com.example.myshoppal.model.Order
import com.example.myshoppal.utils.Constants

class MyOrderListAdapter(private var context: Context,
                         private var list:ArrayList<Order>
    ) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            holder.tvName.text = model.title
            holder.tvPrice.text = "${model.total_amount}"
            holder.ivDelete.visibility = View.GONE

            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .into(holder.iv)
            holder.itemView.setOnClickListener {
                val intent = Intent(context,MyOrderDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_MY_ORDER_DETAILS,model)
                context.startActivity(intent)
            }
        }
    }
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val iv : ImageView = view.findViewById(R.id.iv_item_image)
        val tvName : TextView = view.findViewById(R.id.tv_item_name)
        val tvPrice : TextView = view.findViewById(R.id.tv_item_price)
        val ivDelete : ImageView = view.findViewById(R.id.iv_delete_product)
    }
}