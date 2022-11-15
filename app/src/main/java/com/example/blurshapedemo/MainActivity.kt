package com.example.blurshapedemo

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import com.example.blur_shape.BlurShape
import com.example.blurshapedemo.utils.DensityUtil
import com.example.blurshapedemo.utils.ResourcesUtils
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
        OverScrollDecoratorHelper.setUpStaticOverScroll(
            tvContent, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)
        tvContent.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View, left: Int, top: Int, right: Int, bottom: Int,
                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
            ) {
                v.removeOnLayoutChangeListener(this)
                val shape = BlurShape(clBg, tvContent).apply {
                    val gradient = LinearGradient(
                        0f, v.height.toFloat() / 3 * 2, v.width.toFloat(), v.height.toFloat(),
                        BG_COLOURS, POSITION, Shader.TileMode.CLAMP
                    )
                    setStroke(DensityUtil.dp2px(3f).toFloat(), gradient)
                    setRadius(DensityUtil.dp2px(10f).toFloat())
                }
                v.background = ShapeDrawable(shape)
            }
        })

    }
}