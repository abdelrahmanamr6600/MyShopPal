package com.abdelrahman.myshoppal.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.utils.GlideLoader
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.utils.Constants
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.iv_user_photo
import kotlinx.android.synthetic.main.activity_user_profile.tv_title
import java.io.IOException

class UserProfileActivity : BaseActivity() {
    private lateinit var mUserDetails : com.abdelrahman.myshoppal.models.User
    private var mSelectedImageFileUri:Uri?=null
    private var mUserProfileImageUrl:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

         if (intent.hasExtra(Constants.EXTRA_USER_DETAILS))
         {
             mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
         }
        if (mUserDetails.profileCompleted==0){
            tv_title.text  = "Complete your profile"
            et_email.isEnabled = false
            et_email.setText(mUserDetails.email)

            et_first_name.isEnabled = false
            et_first_name.setText(mUserDetails.firstName)

            et_last_name.isEnabled = false
            et_last_name.setText(mUserDetails.lastName)
            et_mobile_number.setText(""+mUserDetails.mobile)
        }
        else
        {
            setupActionBar()
            tv_title.text = "Profile"
            GlideLoader(this).loadUserPicture(mUserDetails.image,iv_user_photo)
            et_first_name.setText(mUserDetails.firstName)
            et_last_name.setText(mUserDetails.lastName)
            et_email.setText(mUserDetails.email)
            et_mobile_number.setText(mUserDetails.mobile.toString())
            if (mUserDetails.gender==Constants.Male)
            {
             rb_male.isChecked = true
            }
            else if(mUserDetails.gender==Constants.FEMALE){
                rb_female.isChecked = true
            }




        }




 iv_user_photo.setOnClickListener {
     if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
     {
         Constants.showImageChooser(this)
     }
     else
     {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),Constants.READ_STORAGE_PERMISSION_CODE)
     }

 }

        btn_submit.setOnClickListener {
            showProgressBar("Please Wait")
            if (mSelectedImageFileUri!=null)
            {
                FirestoreClass().uploadImageToCloudStorage(this,mSelectedImageFileUri,Constants.userProfileImage)
            }
            else{
                updateUserProfileDetails()
            }
        }

    }

    private fun updateUserProfileDetails(){
        val firstName = et_first_name.text.toString()
        val lastName = et_last_name.text.toString()
        val mobile = et_mobile_number.text.toString()
        val userMap = HashMap<String,Any>()

          if (mUserDetails.firstName != firstName)
          {
              userMap[Constants.First_NAME] = firstName
          }

        if (mUserDetails.lastName != lastName)
        {
            userMap[Constants.LAST_NAME] = lastName
        }

        val gender = if (rb_male.isChecked){
            Constants.Male
        }else
            Constants.FEMALE

        if (mobile.isNotEmpty())
        {
            userMap[Constants.Mobile] = mobile.toLong()

        }
        userMap[Constants.GENDER] = gender
        if (mUserProfileImageUrl.isNotEmpty())
        {
            userMap[Constants.Image] =mUserProfileImageUrl
        }

        userMap[Constants.COMPLETE_PROFILE] = 1
        Log.d("Tag",mUserProfileImageUrl)

        FirestoreClass().updateUserProfile(this,userMap)
    }
    fun userProfileUpdateSuccess(){
        hideDialog()
        startActivity(Intent(this, DashBoardActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==Constants.READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK)
        {
            if (requestCode==Constants.PICK_IMAGE_REQUEST_CODE)
            {
            if (data!=null) {
                try {
                    mSelectedImageFileUri  = data.data!!
                   val glide = GlideLoader(this)
                    glide.loadUserPicture(mSelectedImageFileUri!!,iv_user_photo)
                }catch (e:IOException){
                    e.printStackTrace()
                    Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                }
            }
            }
        }
    }

    fun imageUploadSuccess(imageUrl :String){

       mUserProfileImageUrl = imageUrl

        updateUserProfileDetails()

    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_user_profile_activity)
        val actionbar = supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_user_profile_activity.setNavigationOnClickListener{ onBackPressed() }
    }
}