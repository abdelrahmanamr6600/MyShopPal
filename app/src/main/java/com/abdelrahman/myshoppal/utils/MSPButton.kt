package com.abdelrahman.myshoppal.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.abdelrahman.myshoppal.R

class MSPButton(context : Context, attributeSet : AttributeSet) : AppCompatButton(context , attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont() {

        val typeFace: Typeface = Typeface.createFromAsset(context.assets,"Montserrat-Bold.ttf")
           typeface=typeFace

    }
}