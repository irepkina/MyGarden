package com.irepka3.mygarden.ui.util.context

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use

/**
 * Created by i.repkina on 20.11.2021.
 */
@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(@AttrRes themeAttrId: Int): Int {
    return obtainStyledAttributes(intArrayOf(themeAttrId)).use {
        it.getColor(0, Color.MAGENTA)
    }
}