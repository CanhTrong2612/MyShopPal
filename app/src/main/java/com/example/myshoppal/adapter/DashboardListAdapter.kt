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
import com.example.myshoppal.activities.ProductDetailsActivity
import com.example.myshoppal.model.Product
import com.example.myshoppal.utils.Constants

class DashboardListAdapter(
    var context: Context,
    private var list:ArrayList<Product>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onCliclListener:OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_dashboard_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            holder.tvName.text = model.title
            holder.tvPrice.text = model.price

            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .into(holder.iv)
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.user_id)
                context.startActivity(intent)
            }
        /*    holder.itemView.setOnClickListener{
                if (onCliclListener!=null){
                    onCliclListener!!.onClick(position,model)
                }
            }*/
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv: ImageView = view.findViewById(R.id.iv_dashboard_item_image)
        val tvName: TextView = view.findViewById(R.id.tv_dashboard_item_title)
        val tvPrice: TextView = view.findViewById(R.id.tv_dashboard_item_price)
    }
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onCliclListener = onClickListener
    }
    interface OnClickListener{
        fun onClick(position: Int,product: Product)
    }
}
