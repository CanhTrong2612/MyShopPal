package com.example.myshoppal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.myshoppal.R
import com.example.myshoppal.databinding.ActivitySettingBinding
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.User
import com.example.myshoppal.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class SettingActivity :BaseActivity() ,View.OnClickListener{
    private var binding : ActivitySettingBinding? = null
    private lateinit var mUserDetils :User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionbar()
        binding?.btnLogout?.setOnClickListener(this)
        binding?.edtEdit?.setOnClickListener(this)
        getUserDetails()
        binding?.llAddress?.setOnClickListener {
            startActivity(Intent(this,AddressActivity::class.java))
        }
    }
    fun actionbar(){
        setSupportActionBar(binding?.toolbarSettingsActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title= ""
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24dp)
        binding?.toolbarSettingsActivity?.setTitleTextColor(resources.getColor(R.color.white))
        binding?.toolbarSettingsActivity?.setNavigationOnClickListener { onBackPressed() }
    }
    private fun getUserDetails() {
        showProgressDialog()
        FirestoresClass().getUserDetail(this)
    }
    fun userDetailsSuccess(user: User) {
        mUserDetils=user
        hideProgressDialog()
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .into(binding?.ivUserSeting)

        binding?.tvName?.text = "${user.firstName} ${user.lastName}"
        binding?.tvGender?.text = user.gender
        binding?.tvEmail?.text = user.email
        binding?.tvMobileNumber?.text = "${user.mobie}"
    }
    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.btn_logout->{
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
            R.id.edt_edit->{
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra(Constants.EXTRA_USER_DETAILS,mUserDetils)
                startActivity(intent)


            }
        }
    }
}