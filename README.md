# BlurShape
[![](https://jitpack.io/v/zcxshare/BlurShape.svg)](https://jitpack.io/#zcxshare/BlurShape)

#### 介绍
Android空间毛玻璃、磨砂、高斯模糊背景，带彩色边框，自定义shape

#### 软件架构
给view添加磨砂背景drawable，自定义的shape

![示例图片](demo.gif)

#### 安装教程

###### 1、gradle集成
``` 
	//项目根目录的build.gradle中
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
	//app的build.gradle中
	dependencies {
	        implementation 'com.github.zcxshare:BlurShape:1.1'
	}
``` 

###### 2、自行下载
	
	获取blur_shape module的代码依赖即可，比较简单

#### 使用说明

``` 
        val shape = BlurShape(clBg, tvContent).apply {//第一个参数是以该view为基础进行模糊处理,第二个是需要添加该背景view
            setStroke(DensityUtil.dp2px(3f).toFloat(),getColor(R.color.bg_treat_colours2))//添加边框线
            setRadius(DensityUtil.dp2px(10f).toFloat())//添加倒角
        }
        tvContent.background = ShapeDrawable(shape)
```



#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


