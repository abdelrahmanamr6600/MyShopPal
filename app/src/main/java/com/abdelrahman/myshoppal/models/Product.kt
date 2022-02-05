package com.abdelrahman.myshoppal.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val user_id :String = "",
    val user_name :String = "",
    val title :String = "",
    val price :String = "",
     val  sellerId:String = " ",
    val description :String = "",
    val stock_quantity :String = "",
    val image:String ="",
    var product_id :String = "",

    ): Parcelable
