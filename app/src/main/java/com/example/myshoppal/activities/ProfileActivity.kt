package com.example.myshoppal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.myshoppal.databinding.ActivityProfileBinding
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.User
import com.example.myshoppal.utils.Constants


class ProfileActivity : BaseActivity(), View.OnClickListener{
    private var binding :ActivityProfileBinding?= null
    private lateinit var mUseDeail : User
    private var mSelectImageUri :Uri?= null
    private lateinit var mImageProfile:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()
        binding?.btSaveProfile?.setOnClickListener (this)
    //    FirestoresClass().getUserDetail(this)
        binding?.ivProfile?.setOnClickListener (this)
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            mUseDeail= intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        getDataUser(mUseDeail)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
            showImageChoosen()
        }
        else{
            Toast.makeText(this,
                "You just dennied the permission for storage.You can aslo allow it from setting" ,
                Toast.LENGTH_SHORT).show()
        }
    }
    private fun showImageChoosen() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery,PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK &&
                requestCode == PICK_IMAGE_REQUEST_CODE&&data!!.data!= null)
        {
            mSelectImageUri  = data!!.data
            binding?.ivProfile?.setImageURI(Uri.parse(mSelectImageUri!!.toString()))
        }

    }
    fun getDataUser(user: User){
       if (mUseDeail.profileComplete==0){
           binding?.etFirstnameProfile?.isEnabled = false
           binding?.etFirstnameProfile?.setText(mUseDeail.firstName)

           binding?.etLastnameProfile?.isEnabled = false
           binding?.etLastnameProfile?.setText(mUseDeail.lastName)

           binding?.etmailProfile?.isEnabled = false
           binding?.etmailProfile?.setText(mUseDeail.email)
       }
        else{
           binding?.etFirstnameProfile?.isEnabled = false
           binding?.etFirstnameProfile?.setText(mUseDeail.firstName)

           binding?.etLastnameProfile?.isEnabled = false
           binding?.etLastnameProfile?.setText(mUseDeail.lastName)

           binding?.etmailProfile?.isEnabled = false
           binding?.etmailProfile?.setText(mUseDeail.email)

           binding?.etMobieProfile?.isEnabled = false
           binding?.etMobieProfile?.setText(mUseDeail.mobie.toString())

           if (mUseDeail.gender == Constants.MALE){
               binding?.rbMale?.isChecked == true
           }else{
               binding?.rbPermale?.isChecked == true
           }

           Glide
               .with(this)
               .load(user.image)
               .centerCrop()
               .into(binding?.ivProfile)

       }

    }

    fun actionBar(){
        setSupportActionBar(binding?.toolbarProfile)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("My Profile")
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        binding?.toolbarProfile?.setNavigationOnClickListener { onBackPressed() }
    }

    fun validateUserProfile():Boolean{
        return when{
            TextUtils.isEmpty(binding?.etMobieProfile?.text.toString())->{
                showErrorSnackBar("Please enter mobie number",false)
                false
            }

            else -> {
                true
            }
        }
    }


    override fun onClick(view: View?) {
        when(view?.id){
            R.id.bt_save_profile->{
                if (validateUserProfile()){
                    showProgressDialog()
                    if (mSelectImageUri!=null){
                        FirestoresClass().uploadImageToCloudStorge(this,mSelectImageUri)
                    }else{
                        updateUserProfile()
                    }
                }
            }
            R.id.iv_profile->{
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                    showImageChoosen()
                }else{

                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        READ_STORE_PERMISSION_CODE
                    )
                }
            }

        }
    }
    fun updateUserProfile(){
        val userHashMap = HashMap<String, Any>()
        val mobieNumber = binding?.etMobieProfile?.text.toString()
        if (mobieNumber.isNotEmpty()){
            userHashMap[Constants.MOBIE]=mobieNumber.toLong()
        }
        val gender = if (binding?.rbMale?.isChecked == true){
            Constants.MALE

        } else {
            Constants.FERMALE
        }
        userHashMap[Constants.GENDER]=gender
        userHashMap[Constants.IMAGE] = mImageProfile
        userHashMap[Constants.COMPLETE_PROFILE] = 1
        FirestoresClass().updateUserProfile(this,userHashMap)
    }
    fun imageUploadSuccess(imageUrl:String){
            mImageProfile=imageUrl
            updateUserProfile()
            hideProgressDialog()
    }

    fun updateProfileSuccess() {
        hideProgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
    }

    companion object{
        const val READ_STORE_PERMISSION_CODE = 1
        const val PICK_IMAGE_REQUEST_CODE=2
    }

}