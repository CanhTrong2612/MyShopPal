package com.example.myshoppal.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.CheckBox
import android.widget.Toast
import com.example.myshoppal.R
import com.example.myshoppal.databinding.ActivityRegisterBinding
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {
    private var binding :ActivityRegisterBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()
        binding?.tvloginRegister?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java ))
        }
        binding?.btregister?.setOnClickListener {
            registerUser()
        }
    }
    private fun actionBar(){
        setSupportActionBar(binding?.toolbarregister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title= "CREATE AN ACCOUNT"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        binding?.toolbarregister?.setNavigationOnClickListener { onBackPressed() }

    }

    private fun validateForm(): Boolean{
        return when{
            TextUtils.isEmpty(binding?.etfirstname?.text.toString())->{
                showErrorSnackBar("Please enter First Name.", true)
                false
            }
            TextUtils.isEmpty(binding?.etlastname?.text.toString())->{
                showErrorSnackBar("Please enter Last Name.",true)
                false
            }
            TextUtils.isEmpty(binding?.etmailRegister?.text.toString())->{
                showErrorSnackBar("Please enter Email.", true)
                false
            }
            TextUtils.isEmpty(binding?.etpasswordRegister?.text.toString())->{
                showErrorSnackBar("Please enter Password.", true)
                false
            }
            TextUtils.isEmpty(binding?.etcofirmpasswordRegister?.text.toString())->{
                showErrorSnackBar("Please enter Confirm Password.",true)
                false
            }
            binding?.checkbox?.isChecked == false->{
                showErrorSnackBar("Please agree to the terms and condition",true)
                false

            }
            else -> {
                true
            }

        }
    }
    private fun registerUser(){
        if (validateForm()) {
            showProgressDialog()
            val email = binding?.etmailRegister?.text.toString()
            val password = binding?.etpasswordRegister?.text.toString()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val user = User(
                            firebaseUser.uid,
                            binding?.etfirstname?.text.toString(),
                            binding?.etlastname?.text.toString(),
                            binding?.etmailRegister?.text.toString(),
                        )
                        FirestoresClass().registerUser(this, user)
                        FirebaseAuth.getInstance().signOut()
                        finish()
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
    fun userRegisterSuccess(){
        hideProgressDialog()
        Toast.makeText(this, "You are registered successfully", Toast.LENGTH_SHORT).show()
    }
}