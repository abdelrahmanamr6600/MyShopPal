package com.abdelrahman.myshoppal.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val SharedPreferences = getSharedPreferences(Constants.myShopPreferences,Context.MODE_PRIVATE)
       val name =  SharedPreferences.getString(Constants.Logged_In_Username,"null")
        textView.setText(name)



    }
}


