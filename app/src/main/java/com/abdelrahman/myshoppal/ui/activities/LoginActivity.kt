package com.abdelrahman.myshoppal.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.User
import com.abdelrahman.myshoppal.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        tv_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))

        }
        btn_login.setOnClickListener {
            showProgressBar("Logging in")
            userLogin()
        }
        tv_forgot_password.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }
    }


    private fun userLogin() {
        var email = et_email_forgot_pw.text.toString()
        var password = et_password.text.toString()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                hideDialog()
                if (it.isSuccessful) {

                    var fireStore = FirestoreClass()
                    fireStore.getCurrentUserDetails(this@LoginActivity)
                }
            }
    }


//    override fun onStart() {
//        super.onStart()
//        if (FirebaseAuth.getInstance().currentUser != null) {
//            val goMainActivity= Intent(applicationContext, MainActivity::class.java)
//            startActivity(goMainActivity)
//            finish()
//        }
//
//    }

    fun userLoggedInSuccess(user : User){
        hideDialog()
        Log.d("first name", user.firstName)
        Log.d("last name", user.lastName)
        Log.d("email",user.email)
        if(user.profileCompleted==0){
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
              intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)

        }
        else
        {
            startActivity(Intent(this@LoginActivity, DashBoardActivity::class.java))
        }
        finish()


    }
}