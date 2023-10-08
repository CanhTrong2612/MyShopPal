package com.example.myshoppal.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.example.myshoppal.databinding.ActivityAddProductBinding
import com.example.myshoppal.firebase.FirestoresClass
import com.example.myshoppal.model.Product
import com.example.myshoppal.utils.Constants

class AddProductActivity : BaseActivity(),View.OnClickListener {
    private var binding :ActivityAddProductBinding? = null
    private var mSelectImageUri : Uri?= null
    private var mProductImage :String ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionbar()
        binding?.ivCameraProduct?.setOnClickListener(this)
        binding?.btSubmitProduct?.setOnClickListener(this)
    }
    fun actionbar(){
       setSupportActionBar(binding?.toolbarProduct)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Add Product"
        binding?.toolbarProduct?.setTitleTextColor(resources.getColor(R.color.white))
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24dp)
        binding?.toolbarProduct?.setNavigationOnClickListener { onBackPressed() }

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.iv_camera_product->{
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                    showImageChoosen()
                }else{
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        ProfileActivity.READ_STORE_PERMISSION_CODE
                    )
                }
            }
            R.id.bt_submit_product->{
                showProgressDialog()
                FirestoresClass().uploadImageToCloudStorge(this,mSelectImageUri )
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK &&
            requestCode == ProfileActivity.PICK_IMAGE_REQUEST_CODE &&data!!.data!= null){
                mSelectImageUri=data!!.data
                binding?.ivProduct?.setImageURI(Uri.parse(mSelectImageUri!!.toString()))
        }

    }

    private fun showImageChoosen() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE_REQUEST_CODE)
    }
    fun uploadProductDetails(){
        val username = this.getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES,Context.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME,"")!!
        val product = Product(FirestoresClass().getCurrentID(),
            username,
            binding?.productTitle?.text.toString(),
            binding?.productPrice?.text.toString(),
            binding?.productDescription?.text.toString(),
            binding?.productQuantity?.text.toString(),
            mProductImage
        )
        FirestoresClass().uploadProductDetails(this, product)

    }
    fun productUploadSuccess(){
        hideProgressDialog()
        finish()
    }
    fun imageUploadSuccess(imageUrl:String){
        mProductImage= imageUrl
        uploadProductDetails()
    }
    companion object{
        const val PICK_IMAGE_REQUEST_CODE=2
    }

}