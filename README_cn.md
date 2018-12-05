[English](README.md)  |  中文文档

# EasyGuide
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-11%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=11)
[![Download](https://api.bintray.com/packages/markshawn/com.github.markshawn/easy-guide/images/download.svg)](https://bintray.com/markshawn/com.github.markshawn/easy-guide/_latestVersion)

EasyGuide是一个用Kotlin写成的能轻松添加高亮的新手引导工具，支持对页面中任意View，RecyclerView或ListView的item，甚至是Dialog中的View添加高亮，同时可以在蒙层上的其他位置添加内容。

![demo-gif](https://github.com/Marksss/easy-guide-android/blob/master/gif/demo.gif)
## 用法
### 将以下依赖添加到 `build.gradle` 文件中
```compile 'com.github.markshawn:easy-guide:1.0.2'```
### 基础用法
```
new EasyGuideManager(activity).
    addLayer(
            new CommonGuideLayer(context).addHighlightTarget(yourHighlightView)
    ).show();
```
这样就生成了引导蒙层，其中，addLayer()用来添加单个蒙层，addHighlightTarget()用来添加单个高亮区域，yourHighlightView就是需要高亮突出的view，也可以用Rect传入任意位置
### 设置蒙层添加位置
EasyGuideManager的构造器目前有3种入参：
 - FrameLayout：
 蒙层添加在传入的FrameLayout中； 
 - Activity：
 蒙层添加在传入的Activity中的decorView中，即填满整个屏幕； 
 - Dialog：
 蒙层覆盖在传入的Dialog之上；
### 自定义蒙层
CommonGuideLayer指代一层蒙层，EasyGuide支持添加多层蒙层，只需addLayer()多次即可，同时，单层蒙层支持添加多个高亮区域，只需addHighlightTarget()多次即可。默认点击蒙层任意位置就可以切换蒙层，如果需要自己来控制切换，可以用以下方式：
```
CommonGuideLayer layer = new CommonGuideLayer(context);
...
layer.setOnLayerClickListener(new CommonGuideLayer.OnLayerClickListener() {
    @Override
    public void onClick(int targetIndex, @NonNull ILayerController controller) {
        // If targetIndex < 0, 点击区域在高亮区域外
        // If targetIndex >= 0, 点击区域在高亮区域内，targetIndex高亮区域的按添加顺序的索引
        // controller.goNext(); 下一个蒙层，没有下一个蒙层则隐藏当前蒙层
        // controller.dismiss(); 隐藏所有蒙层
    }
});
```
可以自定义高亮区域的形状：
```
layer.setOnHighLightDrawListener(new CommonGuideLayer.OnHighLightDrawListener() {
    @Override
    public boolean onDraw(int index, @NonNull Rect rect, @NonNull Canvas canvas, @NonNull Paint paint) {
        // canvas.drawCircle(cx, cy, radius, paint); 圆形高亮区域
        // canvas.drawRoundRect(new RectF(rect), r, r, paint); 圆角矩形区域 等等
        ...
    }
});
// layer.setOnHighLightDrawListener(null); 取消高亮
```
可以在蒙层上添加自定义的视图（例如图片或文字）：
```
layer.addHighlightTarget(yourHighlightView).
    withExtraView(customView1, 0, 0, Location.TO_TOP).
    withExtraView(customView2, 0, 50, Location.TO_BOTTOM);
```
其他方法包括：
 - setEnterAnimation：
 设置进入动画；
 - setExitAnimation：
 设置退出动画；
 - setBackgroundColor：
 设置蒙层背景颜色；
 - setOnDismissListener：
 该蒙层退出时的回调；
 - setOnShowListener：
 该蒙层出现时的回调；
## 许可证
EasyGuide基于 [Apache License Version 2.0](LICENSE) 发布。