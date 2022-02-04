package com.abdelrahman.myshoppal.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import com.abdelrahman.myshoppal.R
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen)
     @Suppress("DEPRECATION")
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
           window.insetsController?.hide(WindowInsets.Type.statusBars())
        else{
            window.setFlags(    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        Handler().postDelayed({
            val currentUser = FirebaseAuth.getInstance().currentUser
            if(currentUser != null)
            {
                startActivity(Intent(this, DashBoardActivity::class.java))

            }
            else{
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        },2000)

    }
}