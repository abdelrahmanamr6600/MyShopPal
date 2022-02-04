package com.abdelrahman.myshoppal.ui.activities

import android.app.Dialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.abdelrahman.myshoppal.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity() {
    private lateinit var mProgressBar: Dialog
    private var doubleBackToExitPressedOnce = false


    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                            this@BaseActivity,
                            R.color.colorSnackBarError
                    )
            )
        }else{
            snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                            this@BaseActivity,
                            R.color.colorSnackBarSuccess
                    )
            )
        }
        snackBar.show()
    }

    fun showProgressBar(text : String){
        mProgressBar = Dialog(this)
        mProgressBar.setContentView(R.layout.dialog_progress)
        mProgressBar.setCancelable(false)
        mProgressBar.setCanceledOnTouchOutside(false)
       mProgressBar.tv_progress_text.text = text
        mProgressBar.show()
    }

    fun hideDialog()
    {
        mProgressBar.dismiss()
    }

    fun doubleBackToExit(){
        if (doubleBackToExitPressedOnce)
        {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this,R.string.please_click_back_again_to_exit,Toast.LENGTH_LONG).show()

        @Suppress("DEPRECATION")
        android.os.Handler().postDelayed({
            doubleBackToExitPressedOnce = false

        },2000)

    }

}