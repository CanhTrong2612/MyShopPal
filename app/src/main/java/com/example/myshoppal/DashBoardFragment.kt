package com.example.myshoppal

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.activities.CartListActivity
import com.example.myshoppal.activities.MainActivity
import com.example.myshoppal.activities.ProductDetailsActivity
import com.example.myshoppal.activities.SettingActivity
import com.example.myshoppal.adapter.DashboardListAdapter
import com.example.myshoppal.databinding.FragmentDashBoardBinding
//import com.example.myshoppal.databinding.ActivityBaseBinding.inflate
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.Product
import com.example.myshoppal.utils.Constants

class DashBoardFragment : BaseFragment(), MenuProvider {
    private var binding :FragmentDashBoardBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashBoardBinding.inflate(inflater,container,false)
      //  setFragmentToolbar()
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)
        FirestoresClass().getDashboardList(this@DashBoardFragment)
        return binding?.root
    }

//
//    fun successDashboardItemList(productList: ArrayList<Product>){
//        binding?.rvDashboard?.layoutManager = GridLayoutManager(context,2)
//        binding?.rvDashboard?.setHasFixedSize(true)
//        val adapter = DashboardListAdapter(requireContext(),productList)
//        binding?.rvDashboard?.adapter = adapter
//        adapter.setOnClickListener(object : DashboardListAdapter.OnClickListener{
//            override fun onClick(position: Int, product: Product) {
//                val intent = Intent(context, ProductDetailsActivity::class.java)
//                intent.putExtra(Constants.EXTRA_PRODUCT_ID,product.product_id)
//                intent.putExtra(Constants.CART_QUANTITY,product.quantity)
//               // startActivity(intent)
//            }
//        })
//    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.ic_setting,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_cart -> {
                startActivity(Intent(requireContext(), CartListActivity::class.java))
                true
            }
            R.id.fragment_setting -> {
                startActivity(Intent(requireContext(), SettingActivity::class.java))
                true
            }
            else -> false
        }
    }
}



