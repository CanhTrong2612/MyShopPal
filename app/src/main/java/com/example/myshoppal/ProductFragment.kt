package com.example.myshoppal

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.constraintlayout.compose.State
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.activities.AddProductActivity
import com.example.myshoppal.activities.MainActivity
import com.example.myshoppal.adapter.MyProductAdapter
import com.example.myshoppal.databinding.FragmentProductBinding
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.Product
import kotlin.concurrent.fixedRateTimer

class ProductFragment : BaseFragment(), MenuProvider {
    private var binding :FragmentProductBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= FragmentProductBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
       // getProductList()
       // FirestoresClass().getProductsList(this)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater,container,false)
        setFragmentToolbar()
        val menuHost:MenuHost= requireActivity()
         menuHost.addMenuProvider(this,viewLifecycleOwner,Lifecycle.State.RESUMED)
         getProductList()
       return  binding?.root
    }

    fun setFragmentToolbar(){
        ( requireActivity() as MainActivity ).setSupportActionBar(binding?.toolbarr)
        ( requireActivity() as MainActivity ).supportActionBar!!.title =""
    }
    override fun onResume() {
        super.onResume()
        getProductList()
    }
    fun getProductList(){
      //  showProgressDialog()
        FirestoresClass().getProductsList(this@ProductFragment)

    }

    fun successProductList(product:ArrayList<Product>) {
     //   hideProgressDialog()
        for (i in product ){
            Log.e("product name", i.title)
        }
        if (product.size>0) {
            binding?.rvProduct?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding?.rvProduct?.setHasFixedSize(true)
            val adapter = MyProductAdapter(requireActivity(), product,this)
            binding?.rvProduct?.adapter = adapter
        }
    }
    fun deleteProduct(productID:String){
        showAlertDialogDeleteProduct(productID)
    }
    fun productDeleteSuccess(){
        getProductList()
    }
    fun showAlertDialogDeleteProduct(productID: String){
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle("Delete")
        dialog.setMessage("Are you sure want to delete the product")
        dialog.setIcon(android.R.drawable.ic_dialog_alert)
        dialog.setPositiveButton("Yes"){dialogInterface,which->
           // showProgressDialog()
            FirestoresClass().deleteProduct(this,productID)
            dialogInterface.dismiss()
        }
        dialog.setNegativeButton("No"){dialogInterface,which->
            dialogInterface.dismiss()
        }
        dialog.create()
        dialog.show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.add_product,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                true
            }

            else -> false
        }
    }
}