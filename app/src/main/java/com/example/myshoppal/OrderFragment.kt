package com.example.myshoppal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.adapter.MyOrderListAdapter
import com.example.myshoppal.databinding.FragmentOrderBinding
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.Order


class OrderFragment : BaseFragment() {
    private var binding :FragmentOrderBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater,container,false)
        FirestoresClass().getMyOrderList(this)
        return binding?.root
    }
    fun getMyOrderList(){
        FirestoresClass().getMyOrderList(this)
    }
    fun populateListOrder(list:ArrayList<Order>){
        if (list.size>0){
            binding?.rvFragmentOrder?.layoutManager = LinearLayoutManager(requireActivity())
            binding?.rvFragmentOrder?.setHasFixedSize(true)
            val adapter = MyOrderListAdapter(requireActivity(),list)
            binding?.rvFragmentOrder?.adapter = adapter
        }

    }

}