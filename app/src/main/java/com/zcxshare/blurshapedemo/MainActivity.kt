package com.zcxshare.blurshapedemo

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.zcxshare.blur_shape.BlurShape
import com.example.blurshapedemo.R
import com.zcxshare.blurshapedemo.utils.DensityUtil
import com.zcxshare.blurshapedemo.utils.ResourcesUtils
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class MainActivity : AppCompatActivity() {
    companion object {
        val BG_COLOURS by lazy {
            intArrayOf(
                ResourcesUtils.getColor(R.color.bg_treat_colours1),
                ResourcesUtils.getColor(R.color.bg_treat_colours2),
                ResourcesUtils.getColor(R.color.bg_treat_colours3),
                ResourcesUtils.getColor(R.color.bg_treat_colours4),
                ResourcesUtils.getColor(R.color.bg_treat_colours5),
                ResourcesUtils.getColor(R.color.bg_treat_colours6)
            )
        }
        val POSITION = floatArrayOf(0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ResourcesUtils.init(this)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val clBg = findViewById<View>(R.id.cl_bg)
        val tvContent = findViewById<TextView>(R.id.tv_content)
        val tvContent2 = findViewById<TextView>(R.id.tv_content2)
        val tvContent3 = findViewById<TextView>(R.id.tv_content3)
        OverScrollDecoratorHelper.setUpStaticOverScroll(
            tvContent, OverScrollDecoratorHelper.ORIENTATION_VERTICAL
        )
        OverScrollDecoratorHelper.setUpStaticOverScroll(
            tvContent2, OverScrollDecoratorHelper.ORIENTATION_VERTICAL
        )
        OverScrollDecoratorHelper.setUpStaticOverScroll(
            tvContent3, OverScrollDecoratorHelper.ORIENTATION_VERTICAL
        )
        val shape2 = BlurShape(clBg, tvContent2).apply {
            setStroke(
                DensityUtil.dp2px(3f).toFloat(),
                getColor(R.color.bg_treat_colours2))
            setRadius(DensityUtil.dp2px(10f).toFloat())
        }
        tvContent2.background = ShapeDrawable(shape2)

        val shape3 = BlurShape(clBg, tvContent3).apply {
            setStroke(
                DensityUtil.dp2px(3f).toFloat(),
                ResourcesUtils.getColor(R.color.bg_treat_colours3)
            )
            setRadius(
                DensityUtil.dp2px(0f).toFloat(), DensityUtil.dp2px(20f).toFloat(),
                DensityUtil.dp2px(30f).toFloat(), DensityUtil.dp2px(40f).toFloat())
        }
        tvContent3.background = ShapeDrawable(shape3)

        tvContent.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View, left: Int, top: Int, right: Int, bottom: Int,
                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
            ) {
                v.removeOnLayoutChangeListener(this)
                val gradient = LinearGradient(
                    0f,
                    tvContent.height.toFloat() / 3 * 2,
                    tvContent.width.toFloat(),
                    tvContent.height.toFloat(),
                    BG_COLOURS,
                    POSITION,
                    Shader.TileMode.CLAMP
                )
                val shape = BlurShape(clBg, tvContent).apply {
                    setStroke(DensityUtil.dp2px(3f).toFloat(), gradient)
                    setRadius(DensityUtil.dp2px(10f).toFloat())
                }
                tvContent.paint.shader = gradient
                tvContent.background = ShapeDrawable(shape)
            }
        })

    }
}