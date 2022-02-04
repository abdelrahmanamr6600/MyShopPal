package com.abdelrahman.myshoppal.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView

class MSPEditText(context : Context, attributeSet : AttributeSet) : AppCompatEditText(context , attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont() {

        val faceType : Typeface = Typeface.createFromAsset(context.assets,"Montserrat-Regular.ttf")
        setTypeface(typeface)
     }
}