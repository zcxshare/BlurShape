package com.example.blur_shape

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.text.TextPaint
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.ColorInt
import androidx.annotation.Nullable

class BlurShape(
    private val parentView: View,
    private val selfView: View,
    var cornerRadius: FloatArray = floatArrayOf(
        CORNER_RADIUS_DEFAULT, CORNER_RADIUS_DEFAULT,
        CORNER_RADIUS_DEFAULT, CORNER_RADIUS_DEFAULT,
        CORNER_RADIUS_DEFAULT, CORNER_RADIUS_DEFAULT,
        CORNER_RADIUS_DEFAULT, CORNER_RADIUS_DEFAULT
    ),
    @ColorInt val overlayColor: Int = 0xffffff,
    @Nullable inset: RectF? = null,
    @Nullable innerRadii: FloatArray? = null
) : RoundRectShape(cornerRadius, inset, innerRadii) {
    companion object {
        private const val TAG = "BlurShape"

        @ColorInt
        const val TRANSPARENT = 0
        const val DEFAULT_BLUR_RADIUS = 25f
        const val CORNER_RADIUS_DEFAULT = 25f
    }

    private val blurRadius = DEFAULT_BLUR_RADIUS
    private val renderBlur: RenderBlur by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            RenderEffectBlur()
        } else {
            RenderScriptBlur(getContext())
        }
    }
    private lateinit var internalCanvas: BlurViewCanvas
    private lateinit var internalBitmap: Bitmap


    private val blurEnabled = true
    private var initialized = false

    private var strokeEnabled = false
    private var strokePaint: Paint? = null
    private var frameClearDrawable: Drawable? = null
    private val rootLocation = IntArray(2)
    private val selfLocation = IntArray(2)
    private var lastScaledLeftPosition: Float = 0F
    private var lastScaledTopPosition: Float = 0F
    private var lastUpdateTime: Long = 0L

    private val cornerPath = Path()
    private val mRectF = RectF()


    private val drawListener =
        ViewTreeObserver.OnPreDrawListener {
            updateBlur()
            true
        }

    override fun onResize(w: Float, h: Float) {
        super.onResize(w, h)
        init(w.toInt(), h.toInt())
    }

    fun init(measuredWidth: Int, measuredHeight: Int) {
        setBlurAutoUpdate(true)
        internalBitmap = Bitmap.createBitmap(
            measuredWidth,
            measuredHeight,
            renderBlur.getSupportedBitmapConfig()
        )
        internalCanvas = BlurViewCanvas(internalBitmap)
        initialized = true
        updateBlur()
    }

    private fun setBlurAutoUpdate(enabled: Boolean) {
        parentView.viewTreeObserver.removeOnPreDrawListener(drawListener)
        if (enabled) {
            parentView.viewTreeObserver.addOnPreDrawListener(drawListener)
        }
    }

    private fun updateBlur() {
        if (!blurEnabled || !initialized) {
            return
        }
        val start = System.currentTimeMillis()
        val hasMatrix = setupInternalCanvasMatrix()
        if (hasMatrix) {
            if (frameClearDrawable == null) {
                internalBitmap.eraseColor(Color.TRANSPARENT)
            } else {
                frameClearDrawable?.draw(internalCanvas)
            }
            if (parentView.background != null) {
                parentView.background.draw(internalCanvas)
            } else {
                parentView.draw(internalCanvas)
            }
            internalCanvas.restore()
            blurAndSave()
            lastUpdateTime = System.currentTimeMillis()
        }
        val end = System.currentTimeMillis()
        Log.i(TAG, "updateBlur: ${end - start}")
    }

    fun setFrameClearDrawable(frameClearDrawable: Drawable) {
        this.frameClearDrawable = frameClearDrawable
    }

    private fun setupInternalCanvasMatrix(): Boolean {
        parentView.getLocationOnScreen(rootLocation)
        selfView.getLocationOnScreen(selfLocation)
        val left: Int = selfLocation[0] - rootLocation[0]
        val top: Int = selfLocation[1] - rootLocation[1]
        val scaleFactorH: Float = height / internalBitmap.height
        val scaleFactorW: Float = width / internalBitmap.width
        val scaledLeftPosition = -left / scaleFactorW
        val scaledTopPosition = -top / scaleFactorH
        if (lastScaledLeftPosition == scaledLeftPosition && lastScaledTopPosition == scaledTopPosition) {
            return false
        }
        lastScaledLeftPosition = scaledLeftPosition
        lastScaledTopPosition = scaledTopPosition
        internalCanvas.save()
        internalCanvas.translate(scaledLeftPosition, scaledTopPosition)
        internalCanvas.scale(1 / scaleFactorW, 1 / scaleFactorH)
        return true
    }

    private fun blurAndSave() {
        internalBitmap = renderBlur.blur(internalBitmap, blurRadius)
        if (!renderBlur.canModifyBitmap()) {
            internalCanvas.setBitmap(internalBitmap)
        }
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        if (!blurEnabled || !initialized) {
            return
        }
        if (canvas is BlurViewCanvas) {
            return
        }

        val scaleFactorH: Float = height / internalBitmap.height
        val scaleFactorW: Float = width / internalBitmap.width
        drawCorner(canvas)
        canvas.save()
        canvas.scale(scaleFactorW, scaleFactorH)
        renderBlur.render(canvas, internalBitmap)
        canvas.restore()
        if (overlayColor != TRANSPARENT) {
            canvas.drawColor(overlayColor)
        }
        drawStroke(canvas)
    }

    private fun drawCorner(canvas: Canvas) {
        mRectF.right = width
        mRectF.bottom = height
        cornerPath.addRoundRect(mRectF, cornerRadius, Path.Direction.CW)
        cornerPath.close()
        canvas.clipPath(cornerPath)
    }


    private fun drawStroke(canvas: Canvas) {
        if (!strokeEnabled || strokePaint == null) return
        canvas.drawPath(cornerPath, strokePaint!!)
    }

    fun setStroke(width: Float, @ColorInt color: Int) {
        setStroke(width, SweepGradient(0f, 0f, color, color))
    }

    /**
     * 设置边框线宽度
     */
    fun setStroke(width: Float, shader: Shader) {
        strokeEnabled = true
        strokePaint = Paint().apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            setShader(shader)
            strokeWidth = width
        }
    }

    fun setRadius(radius: Float) {
        setRadius(radius, radius, radius, radius)
    }

    /**
     * 设置倒角半径
     */
    fun setRadius(topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        cornerRadius[0] = topLeft
        cornerRadius[1] = topLeft
        cornerRadius[2] = topRight
        cornerRadius[3] = topRight
        cornerRadius[4] = bottomRight
        cornerRadius[5] = bottomRight
        cornerRadius[6] = bottomLeft
        cornerRadius[7] = bottomLeft
    }

    private fun getContext(): Context {
        return selfView.context
    }
}