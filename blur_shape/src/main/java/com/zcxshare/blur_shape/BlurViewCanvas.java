package com.zcxshare.blur_shape;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import androidx.annotation.NonNull;

public class BlurViewCanvas extends Canvas {
    public BlurViewCanvas(@NonNull Bitmap bitmap) {
        super(bitmap);
    }
}
