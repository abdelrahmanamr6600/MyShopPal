package com.abdelrahman.myshoppal.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.abdelrahman.myshoppal.R
import com.abdelrahman.myshoppal.firestore.FirestoreClass
import com.abdelrahman.myshoppal.models.Product
import com.abdelrahman.myshoppal.utils.Constants
import com.abdelrahman.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.IOException

class AddProductActivity : BaseActivity() {
   private var mSelectedImageFileUri: Uri?=null
    private var mProductImageUrl:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        setupActionBar()
        btn_submit_add_product.setOnClickListener {
            showProgressBar("Please Wait")
            if (mSelectedImageFileUri!=null)
            {
                FirestoreClass().uploadImageToCloudStorage(this,mSelectedImageFileUri,Constants.PRODUCT_IMAGE)
            }
            else{
               uploadProductDetails()
            }
        }
        iv_add_update_product.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            {
                Constants.showImageChooser(this)
            }
            else
            {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),Constants.READ_STORAGE_PERMISSION_CODE)

            }
        }


    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_add_product_activity)
        val actionbar = supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_add_product_activity.setNavigationOnClickListener{ onBackPressed() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==Constants.READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Constants.showImageChooser(this)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK)
        {
            if (requestCode==Constants.PICK_IMAGE_REQUEST_CODE)
            {
                if (data!=null) {


                    try {
                        mSelectedImageFileUri = data.data!!
                        val glide = GlideLoader(this)
                        glide.loadUserPicture(mSelectedImageFileUri!!,iv_product_image)
                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this,"Error", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

    private fun uploadProductImage(){

        if (mSelectedImageFileUri!=null)
        {
            FirestoreClass().uploadImageToCloudStorage(this,mSelectedImageFileUri,Constants.PRODUCT_IMAGE)
        }
    }

    private fun uploadProductDetails(){

       var title = et_product_title.text.toString()
        val sharedPreference =  getSharedPreferences(Constants.myShopPreferences, Context.MODE_PRIVATE)
         var userName =  sharedPreference.getString(Constants.Logged_In_Username,"")
        var price = et_product_price.text.toString()
        var description = et_product_description.text.toString()
        var stockQuantity = et_product_quantity.text.toString()
        val product = Product(
            FirestoreClass().getCurrentUser(),
            userName!!,
            title,
            price,
            description,
            stockQuantity,
            mProductImageUrl
        )
        FirestoreClass().addProduct(this,product)
    }

    fun imageUploadSuccess(imageUrl :String){
        mProductImageUrl = imageUrl
        uploadProductDetails()
    }

    fun productAddedSuccess() {

        // Hide the progress dialog
        hideDialog()
        // Finish the Register Screen
        finish()
    }

}