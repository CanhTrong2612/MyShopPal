package com.example.myshoppal.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.example.myshoppal.R
import com.example.myshoppal.databinding.ActivityLoginBinding
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.User
import com.example.myshoppal.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() , View.OnClickListener{
    private var binding : ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.btlogin?.setOnClickListener (this)
        binding?.tvForgotpassword?.setOnClickListener (this)
        binding?.tvregisterLogin?.setOnClickListener (this)

    }
    private fun loginUsers()
    {
        val email = binding?.etmailLogin?.text.toString()
        val password = binding?.etPasswordLogin?.text.toString()
        if (validateForm()){
       showProgressDialog()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    FirestoresClass().getUserDetail(this)

                } else {
                    hideProgressDialog()
                    showErrorSnackBar("login fail", true)

                }
            }
            }
    }
        fun validateForm(): Boolean{
        return when{
            TextUtils.isEmpty(binding?.etmailLogin?.text.toString())->{
                showErrorSnackBar("Please enter email.", true)
                false
            }
            TextUtils.isEmpty(binding?.etPasswordLogin?.text.toString())->{
                showErrorSnackBar("Please enter Last Name.",true)
                false
            }
            else->{
                true
            }
            }
        }

    override fun onClick(view: View?) {
      if (view!=null){
          when(view.id)
          {
              R.id.btlogin ->{
                  loginUsers()
              }
              R.id.tvregister_login ->{
                  startActivity(Intent(this, RegisterActivity::class.java))
              }
              R.id.tv_forgotpassword->{
                  startActivity(Intent(this, ForgotPasswordActivity::class.java))
              }
          }
      }
    }

    fun userLoggedInSuccess(user: User) {
        hideProgressDialog()
        if (user.profileComplete==0){
            val intent = (Intent(this, ProfileActivity::class.java))
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }else{
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}