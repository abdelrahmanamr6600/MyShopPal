package com.abdelrahman.myshoppal.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val Users :String = "users"
    const val SELLER_ID = "sellerId"
    const val Products:String = "products"
    const val myShopPreferences : String = "MyShopPref"
    const val Logged_In_Username  = "Logged_in_username"
    const val EXTRA_USER_DETAILS = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val Male :String = "male"
    const val First_NAME :String = "firstName"
    const val LAST_NAME:String = "lastName"
    const val FEMALE :String = "female"
    const val Mobile :String = "mobile"

    const val GENDER :String = "gender"
    const val Image :String = "image"
    const val userProfileImage:String="User_Profile_Image"
    const val PRODUCT_IMAGE = "Product_Image"
    const val COMPLETE_PROFILE :String = "profileCompleted"
    const val USER_ID :String  = "user_id"
    const val USER_D :String  = "userId"
    const val PRODUCT_ID = "product_id"
    const val DEFAULT_CART_QUANTITY : String = "1"
    const val CART_ITEMS :String = "cart_items"
    const val CART_QUANTITY : String = "cart_Quantity"
    const val HOME: String = "Home"
    const val OFFICE: String = "Office"
    const val OTHER: String = "Other"
    const val ADDRESSES = "addresses"
    const val Extra_Address_Details:String = "AddressDetails"
    const val Extra_SELECT_ADDRESS:String = " extra_select_address"
    const val Extra_SELECTED_ADDRESS:String = " extra_selected_address"
    const val Add_Address_REQUEST_CODE:Int = 121
     const val ORDERS:String = "orders"
    const val STOCK_QUANTITY:String = "stock_quantity"
    const val EXTRA_MY_ORDER_DETAILS: String = "extra_my_order_details"
    const val SOLD_PRODUCTS: String = "sold_products"
    const val EXTRA_SOLD_PRODUCT_DETAILS: String = "extra_sold_product_details"


    fun showImageChooser(activity:Activity)
    {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
    fun getFileExtensions(activity:Activity,uri:Uri?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}