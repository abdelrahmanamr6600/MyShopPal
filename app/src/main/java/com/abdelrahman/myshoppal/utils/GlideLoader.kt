package com.abdelrahman.myshoppal.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader(val context: Context) {
    fun loadUserPicture(imageURI:Any ,ImageView:ImageView){
        try {
            Glide
                .with(context)
                .load(Uri.parse(imageURI.toString()))
                .centerCrop()
                .into(ImageView)
        }
        catch (e:IOException){
            e.printStackTrace()
        }
    }

    fun loadProductPicture(imageURI:Any ,ImageView:ImageView){
        try {
            Glide
                    .with(context)
                    .load(Uri.parse(imageURI.toString()))
                    .centerCrop()
                    .into(ImageView)
        }
        catch (e:IOException){
            e.printStackTrace()
        }
    }
}