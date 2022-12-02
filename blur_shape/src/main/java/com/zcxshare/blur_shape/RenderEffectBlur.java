package com.zcxshare.blur_shape;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RenderEffect;
import android.graphics.RenderNode;
import android.graphics.Shader;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(Build.VERSION_CODES.S)
public class RenderEffectBlur implements RenderBlur {
    private static final float DEFAULT_SCALE_FACTOR = 6f;
    private final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
    private final RenderNode node = new RenderNode("BlurViewNode");

    private float lastBlurRadius;
    private int height, width;
    private Bitmap bitmap;

    public RenderEffectBlur() {
    }

    @Override
    public Bitmap blur(Bitmap bitmap, float blurRadius) {
        if (bitmap.getHeight() != height || bitmap.getWidth() != width) {
            height = bitmap.getHeight();
            width = bitmap.getWidth();
            node.setPosition(0, 0, width, height);
        }
        Canvas canvas = node.beginRecording();
        canvas.drawBitmap(bitmap, 0, 0, null);
        node.endRecording();
        if (lastBlurRadius != blurRadius) {
            node.setRenderEffect(RenderEffect.createBlurEffect(blurRadius, blurRadius, Shader.TileMode.MIRROR));
            lastBlurRadius = blurRadius;
        }
        // returning not blurred bitmap, because the rendering relies on the RenderNode
        this.bitmap = bitmap;
        return bitmap;
    }

    @Override
    public boolean canModifyBitmap() {
        return true;
    }

    @NonNull
    @Override
    public Bitmap.Config getSupportedBitmapConfig() {
        return Bitmap.Config.ARGB_8888;
    }

    @Override
    public void render(@NonNull Canvas canvas, @NonNull Bitmap ignored) {
        if (canvas.isHardwareAccelerated()) {
            canvas.drawRenderNode(node);
        } else {
            canvas.drawBitmap(ignored, 0f, 0f, paint);
        }
    }

    @Override
    public void onResume() {
        if (bitmap != null) {
            Canvas recordingCanvas = node.beginRecording();
            recordingCanvas.drawBitmap(bitmap, 0, 0, null);
            node.endRecording();
        }
    }
}
