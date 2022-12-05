package com.zcxshare.blurshapedemo

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.blurshapedemo.R
import com.zcxshare.blur_shape.BlurShape
import com.zcxshare.blurshapedemo.utils.DensityUtil
import com.zcxshare.blurshapedemo.utils.ResourcesUtils
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
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

    private val clBg: View
        get() {
            val clBg = findViewById<View>(R.id.cl_bg)
            return clBg
        }

    private fun initView() {
        val clBg = clBg
        val rvItems = findViewById<RecyclerView>(R.id.rv_items)
        val tvContent = findViewById<TextView>(R.id.tv_content)
        val tvContent2 = findViewById<TextView>(R.id.tv_content2)
        val tvContent3 = findViewById<TextView>(R.id.tv_content3)
        initRv(rvItems)
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
                getColor(R.color.bg_treat_colours2)
            )
            setRadius(DensityUtil.dp2px(10f).toFloat())
        }
        tvContent2.background = ShapeDrawable(shape2)

        val shape3 = BlurShape(clBg!!, tvContent3).apply {
            setStroke(
                DensityUtil.dp2px(3f).toFloat(),
                ResourcesUtils.getColor(R.color.bg_treat_colours3)
            )
            setRadius(
                DensityUtil.dp2px(0f).toFloat(), DensityUtil.dp2px(20f).toFloat(),
                DensityUtil.dp2px(30f).toFloat(), DensityUtil.dp2px(40f).toFloat()
            )
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

    private fun initRv(rvItems: RecyclerView) {
        rvItems.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val textView = TextView(parent.context)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(20)
                textView.layoutParams = layoutParams
                textView.setPadding(50)
                textView.gravity = Gravity.CENTER
                return MyViewHolder(textView)
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val itemView = holder.itemView as TextView
                itemView.text = position.toString()
                if (itemView.background !is ShapeDrawable) {
                    val shape2 = BlurShape(clBg, holder.itemView).apply {
                        setStroke(
                            DensityUtil.dp2px(3f).toFloat(),
                            getColor(R.color.bg_treat_colours2)
                        )
                        setRadius(DensityUtil.dp2px(10f).toFloat())
                    }
                    itemView.background = ShapeDrawable(shape2)
                }
                val hashCode = (itemView.background as ShapeDrawable).shape.hashCode()
                Log.i(TAG, "onBindViewHolder: position:$position Shape:$hashCode")
            }

            override fun getItemCount(): Int {
                return 20
            }
        }
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}
