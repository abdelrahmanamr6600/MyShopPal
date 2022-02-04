package com.abdelrahman.myshoppal.ui.activities

import android.content.Intent
import android.os.Bundle
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.User
import com.abdelrahman.myshoppal.utils.Constants
import com.abdelrahman.myshoppal.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {
    private lateinit var mUserDetails : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupActionBar()
        getUserDetails()
        logoutUser()
        editProfile()
        goToAddressActivity()
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_settings_activity)
        val actionbar = supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_settings_activity.setNavigationOnClickListener{ onBackPressed() }
    }
    private fun getUserDetails(){
        showProgressBar("pleas wait")
        FirestoreClass().getCurrentUserDetails(this)
    }

    fun userDetailsSuccess(user:User){
        mUserDetails = user
        hideDialog()
        GlideLoader(this).loadUserPicture(user.image,iv_user_photo)
        tv_name.text = "${user.firstName} ${user.lastName}"
        tv_email.text = user.email
        tv_gender.text = user.gender
        tv_mobile_number.text = "${user.mobile }"
    }
    private fun logoutUser(){
        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags=  Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
    private fun editProfile(){
        btn_edit.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,mUserDetails)
            startActivity(intent)
        }
    }

    private fun goToAddressActivity(){
        ll_address.setOnClickListener {
            val intent = Intent(this, AddressListActivity::class.java)
            startActivity(intent)    }

    }
}