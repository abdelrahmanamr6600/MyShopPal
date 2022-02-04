package com.abdelrahman.myshoppal.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.abdelrahman.myshoppal.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPassword : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setupActionBar()

        btn_submit.setOnClickListener {
            showProgressBar("Loading")
            resetPassword()
        }
    }


    private fun setupActionBar() {

        setSupportActionBar(toolbar_forgot_password_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = ""
        }

        toolbar_forgot_password_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun resetPassword(){
        var email = et_email_forgot_pw.text.toString()
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
            hideDialog()

            if (it.isSuccessful)
              {
                  startActivity(Intent(this, LoginActivity::class.java))
                  finish()
              }
            else
            {
                Toast.makeText(this, it.exception?.message.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }
}