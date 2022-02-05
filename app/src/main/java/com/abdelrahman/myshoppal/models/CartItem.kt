package com.abdelrahman.myshoppal.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem(
        val user_id :String = " ",
        var productId :String = " ",
        val title :String = " ",
        val price :String = " ",
        val image :String = " ",
        val seller_id:String = " ",
        var cart_Quantity :String = " ",
        var stock_Quantity:String = " ",
        var id :String = " ",
) : Parcelable {

}
