package com.example.blur_shape

import android.graphics.Bitmap
import android.graphics.Canvas

interface RenderBlur {
    fun blur(bitmap: Bitmap, blurRadius: Float): Bitmap
    fun canModifyBitmap(): Boolean
    fun getSupportedBitmapConfig(): Bitmap.Config
    fun render(canvas: Canvas, bitmap: Bitmap)
    fun onResume()
}