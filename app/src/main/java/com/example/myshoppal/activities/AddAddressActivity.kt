package com.example.myshoppal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.myshoppal.R
import com.example.myshoppal.databinding.ActivityAddAddressBinding
import com.example.myshoppal.databinding.ActivityAddProductBinding
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.Address
import com.example.myshoppal.utils.Constants

class AddAddressActivity :BaseActivity() {
    private var binding : ActivityAddAddressBinding? = null
    private var mAddress:Address ? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()

        binding?.btnSubmitAddress?.setOnClickListener {
            saveAddress()
        }
        binding?.rgType?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rb_other){
                binding?.tilOtherDetails?.visibility = View.VISIBLE
            }
            else{
                binding?.tilOtherDetails?.visibility = View.GONE
            }
        }
        if (intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)){
            mAddress= intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)
        }
        if (mAddress!= null ) {
            if (mAddress!!.id.isNotEmpty()) {

                binding?.etFullName?.setText(mAddress!!.name)
                binding?.etPhoneNumber?.setText(mAddress!!.mobileNumber)
                binding?.etAddress?.setText(mAddress!!.address)
                binding?.etZipCode?.setText(mAddress!!.zipcode)
                binding?.etAdditionalNote?.setText(mAddress!!.additionalNote)
                binding?.etOtherDetails?.setText(mAddress!!.otherDetail)
                binding?.btnSubmitAddress?.text = "Update"
                binding?.tvTitle?.text = "Edit Address"
                if (binding?.rbHome?.isChecked == true) {
                    mAddress!!.type = Constants.HOME
                }
                if (binding?.rbOffice?.isChecked == true) {
                    mAddress!!.type = Constants.OFFICE
                }
                if (binding?.rbOther?.isChecked == true) {
                    binding?.etOtherDetails?.visibility = View.VISIBLE
                }
            }
        }
    }
    fun setupActionBar(){
        setSupportActionBar(binding?.toolbarAddEditAddressActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24dp)
        binding?.toolbarAddEditAddressActivity?.setNavigationOnClickListener { onBackPressed() }
    }
    fun addAddressSuccess(){
        Toast.makeText(this, "Add the address successfully",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,AddressActivity::class.java))
        setResult(RESULT_OK)
        finish()
    }
    fun saveAddress() {
        val fullName = binding?.etFullName?.text.toString().trim()
        val phoneNumber = binding?.etPhoneNumber?.text.toString().trim()
        val address = binding?.etAddress?.text.toString().trim()
        val zipcode = binding?.etZipCode?.text.toString().trim()
        val additionalNote = binding?.etAdditionalNote?.text.toString().trim()
        val otherDetail = binding?.etOtherDetails?.text.toString().trim()
        if (validateForm()) {
            val addressType: Any = when{
                binding?.rbHome?.isChecked == true->{
                    Constants.HOME
                }
                binding?.rbOffice?.isChecked==true-> {
                    Constants.OFFICE
                }
                else->true
            }
            val address = Address(
                FirestoresClass().getCurrentID(),
                fullName,
                phoneNumber,
                address,
                zipcode,
                additionalNote,
                addressType as String,
                otherDetail
            )
            if (mAddress!= null && mAddress!!.id.isNotEmpty()){
                FirestoresClass().updateAddress(this,address,mAddress!!.id)

            }
            else{
                FirestoresClass().addAddress(this, address)
            }


        }

    }
    fun validateForm(): Boolean {
        when{
            TextUtils.isEmpty(binding?.etFullName?.text.toString())->{
                showErrorSnackBar("Please enter full name",true)
            }
            TextUtils.isEmpty(binding?.etPhoneNumber?.text.toString())->{
                showErrorSnackBar("Please enter phone number",true)
            }
            TextUtils.isEmpty(binding?.etAddress?.text.toString())->{
                showErrorSnackBar("Please enter address",true)
            }
            TextUtils.isEmpty(binding?.etZipCode?.text.toString())->{
                showErrorSnackBar("Please enter code",true)
            }
            TextUtils.isEmpty(binding?.etAdditionalNote?.text.toString())->{
                showErrorSnackBar("Please enter additional note",true)
            }

        }
        return true
    }


}