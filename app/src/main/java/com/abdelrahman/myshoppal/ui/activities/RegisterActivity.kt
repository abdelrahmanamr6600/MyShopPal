package com.abdelrahman.myshoppal.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass

import com.abdelrahman.myshoppal.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        else{
            window.setFlags(    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
       tv_login.setOnClickListener {
           startActivity(Intent(this, LoginActivity::class.java))
           finish()
       }
        btn_register.setOnClickListener{
            showProgressBar("Registering")
            userRegister()
        }
    }


    private fun userRegister(){
        val email =  et_email.text.toString()
        val password = et_password.text.toString()

       FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
           .addOnCompleteListener { task ->

               if (task.isSuccessful)
               {
                  val  fireBaseUser : FirebaseUser = task.result!!.user!!
                   hideDialog()
                   val user = User(
                       FirebaseAuth.getInstance().uid!!,
                        et_first_name.text.toString().trim(){it <= ' '},
                       et_last_name.text.toString().trim(){it <= ' '},
                       et_email.text.toString().trim(){it <= ' '},
                   )
                    val fireStore = FirestoreClass()

                   fireStore.registerUser(this@RegisterActivity,user)


                   Toast.makeText(this@RegisterActivity,fireBaseUser.uid,Toast.LENGTH_LONG).show();

               }


           }.addOnFailureListener {
               Toast.makeText(this,"error",Toast.LENGTH_LONG).show()
           }
    }

    fun userRegistrationSuccess() {

        // Hide the progress dialog
      hideDialog()
        FirebaseAuth.getInstance().signOut()
        // Finish the Register Screen
        finish()
    }
}