package com.example.myshoppal.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    private lateinit var mProressDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

    }

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            message, Snackbar.LENGTH_SHORT
        )
        val snackbarView = snackbar.view
        if (errorMessage) {
            snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.pink))
            snackbar.show()
        } else {
            snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_700))
            snackbar.show()
        }
    }

    fun showProgressDialog() {
        mProressDialog = Dialog(this)
        mProressDialog.setContentView(R.layout.dialog_progress)
        val tvDialog: TextView = mProressDialog.findViewById(R.id.tv_progress_text)
        tvDialog.text = "Please Wait..."
        mProressDialog.show()
    }

    fun hideProgressDialog() {
        mProressDialog.dismiss()
    }
}