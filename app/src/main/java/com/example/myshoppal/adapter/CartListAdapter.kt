package com.example.myshoppal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myshoppal.R
import com.example.myshoppal.activities.CartListActivity
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.CartItem
import com.example.myshoppal.utils.Constants

class CartListAdapter(private var context: Context,
                      private var list: ArrayList<CartItem>,
                      private var updateCartItem:Boolean

) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder){
            val model = list[position]
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .into(holder.iv)

            holder.tvTitle.text = model.title
            holder.tvPrice.text ="${model.price}"
            holder.tvQuantity.text = model.quantity
            if (updateCartItem) {
                holder.ibRemove.visibility = View.VISIBLE
                holder.ibAdd.visibility = View.VISIBLE
                holder.ibDeleteCart.visibility = View.VISIBLE


                holder.ibDeleteCart.setOnClickListener {
                    if (context is CartListActivity) {
                        (context as CartListActivity).deleteCart(model.id)
                    }
                }
                holder.ibRemove.setOnClickListener {
                    if (model.quantity == "1") {
                        FirestoresClass().removeCart(context as CartListActivity, model.id)
                    } else {
                        val cartQuantity: Int = model.quantity.toInt()
                        val itemHashMap = HashMap<String, Any>()
                        itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()
                        FirestoresClass().updateMyCart(context, model.id, itemHashMap)
                    }
                }
                holder.ibAdd.setOnClickListener {
                    val cartQuantity: Int = model.quantity.toInt()
                    val itemHashMap = HashMap<String, Any>()
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()
                    FirestoresClass().updateMyCart(context, model.id, itemHashMap)
                }
            }
            else{
                holder.ibRemove.visibility = View.GONE
                holder.ibAdd.visibility = View.GONE
                holder.ibDeleteCart.visibility = View.GONE
            }
        }
    }
    class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        val iv :ImageView = view.findViewById(R.id.iv_cart_item_image)
        val tvTitle: TextView = view.findViewById(R.id.tv_cart_item_title)
        val tvPrice:TextView = view.findViewById(R.id.tv_cart_item_price)
        val tvQuantity :TextView = view.findViewById(R.id.tv_cart_quantity)
        val ibAdd:ImageButton = view.findViewById(R.id.ib_add_cart_item)
        val ibRemove :ImageButton = view.findViewById(R.id.ib_remove_cart_item)
        val ibDeleteCart :ImageButton = view.findViewById(R.id.ib_delete_cart_item)

    }
}