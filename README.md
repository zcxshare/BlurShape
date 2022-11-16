# BlurShape

#### 介绍
Android空间毛玻璃、磨砂、高斯模糊背景，带彩色边框，自定义shape

#### 软件架构
给view添加磨砂背景drawable，自定义的shape

![示例图片](demo.gif)

#### 安装教程

自行下载后获取blur_shape module的代码依赖即可，比较简单

#### 使用说明

``` 
view.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View, left: Int, top: Int, right: Int, bottom: Int,
                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
            ) {
                v.removeOnLayoutChangeListener(this)
                val shape = BlurShape(blurParentView, view).apply { //第一个参数是以该view为基础进行模糊处理,第二个是需要添加该背景view
                    val gradient = LinearGradient(
                        0f, v.height.toFloat() / 3 * 2, v.width.toFloat(), v.height.toFloat(),
                        BG_COLOURS, POSITION, Shader.TileMode.CLAMP
                    ) 
                    setStroke(DensityUtil.dp2px(3f).toFloat(), gradient)//添加边框线
                    setRadius(DensityUtil.dp2px(10f).toFloat()) //添加倒角
                }
                v.background = ShapeDrawable(shape) //将背景设置到view上
            }
        })
```



#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


