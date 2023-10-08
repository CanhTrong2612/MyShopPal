package com.example.myshoppal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myshoppal.DashBoardFragment
import com.example.myshoppal.OrderFragment
import com.example.myshoppal.ProductFragment
import com.example.myshoppal.R
import com.example.myshoppal.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        replaceFragment(DashBoardFragment())
        binding?.bottomNavigation?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_dashboard->{
                    replaceFragment(DashBoardFragment())
                    true
                }
                R.id.navigation_products->{
                    replaceFragment(ProductFragment())
                    true
                }
                R.id.navigation_notifications->{
                    replaceFragment(OrderFragment())
                    true
                }

                else -> {false}
            }
        }

    }
    fun replaceFragment(fragment:Fragment){
        val fragmentManager =supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}