package com.zcxshare.blur_shape;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import androidx.annotation.NonNull;


public final class RenderScriptBlur implements RenderBlur {
    float DEFAULT_SCALE_FACTOR = 6f;
    private final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
    private final RenderScript renderScript;
    private final ScriptIntrinsicBlur blurScript;
    private Allocation outAllocation;

    private int lastBitmapWidth = -1;
    private int lastBitmapHeight = -1;
    private float lastBlurRadius;
    private Allocation inAllocation;

    /**
     * @param context Context to create the {@link RenderScript}
     */
    public RenderScriptBlur(Context context) {
        renderScript = RenderScript.create(context);
        blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
    }

    private boolean canReuseAllocation(Bitmap bitmap) {
        return bitmap.getHeight() == lastBitmapHeight && bitmap.getWidth() == lastBitmapWidth;
    }

    /**
     * @param bitmap     bitmap to blur
     * @param blurRadius blur radius (1..25)
     * @return blurred bitmap
     */
    public Bitmap blur(Bitmap bitmap, float blurRadius) {

        if (blurRadius != lastBlurRadius) {
            blurScript.setRadius(blurRadius);
            this.lastBlurRadius = blurRadius;
        }
        boolean canReuseAllocation = canReuseAllocation(bitmap);
        if (!canReuseAllocation) {
            //Allocation will use the same backing array of pixels as bitmap if created with USAGE_SHARED flag
            if (inAllocation != null){
                inAllocation.destroy();
            }
            inAllocation = Allocation.createFromBitmap(renderScript, bitmap);
            blurScript.setInput(inAllocation);
            if (outAllocation != null) {
                outAllocation.destroy();
            }
            outAllocation = Allocation.createTyped(renderScript, inAllocation.getType());
            lastBitmapWidth = bitmap.getWidth();
            lastBitmapHeight = bitmap.getHeight();
        }else {
            inAllocation.copyFrom(bitmap);
        }

        //do not use inAllocation in forEach. it will cause visual artifacts on blurred Bitmap
        blurScript.forEach(outAllocation);
        outAllocation.copyTo(bitmap);
        return bitmap;
    }

    public final void destroy() {
        blurScript.destroy();
        renderScript.destroy();
        if (outAllocation != null) {
            outAllocation.destroy();
        }
    }

    public boolean canModifyBitmap() {
        return true;
    }

    @NonNull
    public Bitmap.Config getSupportedBitmapConfig() {
        return Bitmap.Config.ARGB_8888;
    }

    public float scaleFactor() {
        return DEFAULT_SCALE_FACTOR;
    }

    public void render(@NonNull Canvas canvas, @NonNull Bitmap bitmap) {
        canvas.drawBitmap(bitmap, 0f, 0f, paint);
    }

    @Override
    public void onResume() {

    }
}
