English  |  [中文文档](README_cn.md)

# EasyGuide
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-11%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=11)
[![Download](https://api.bintray.com/packages/markshawn/com.github.markshawn/easy-guide/images/download.svg)](https://bintray.com/markshawn/com.github.markshawn/easy-guide/_latestVersion)

EasyGuide is an easy-to-use tool to create guide layer on Android using Kotlin, which supports adding highlight on any View on screen, or any item in RecyclerView or ListView, or any area in dialog. And adding any custom view on the layer around the highlight area is also supported.

![demo-gif](https://github.com/Marksss/easy-guide-android/blob/master/gif/demo.gif)
## Usage
### Add the dependency to your project `build.gradle` file
```compile 'com.github.markshawn:easy-guide:1.0.2'```
### Basic Usage
```
new EasyGuideManager(activity).
    addLayer(
            new CommonGuideLayer(context).addHighlightTarget(yourHighlightView)
    ).show();
```
Then the guide layer will appear. As shown above, addLayer() is used to add a particuler layer and addHighlightTarget() a particuler highlight area. The yourHighlightView is a view's area that needs to highlight and it is also supported to highlight any location with Rect.
### Set the layer's location
There have been 3 kinds of parameter in the constrcuctor of EasyGuideManager so far as below:
 - FrameLayout:
 Adding layer to that FrameLayout and filling the entire FrameLayout;
 - Activity:
 Adding layer to decorView in Activity and filling the entire screen;
 - Dialog:
 Covering Dialog with the layer;
### Customize layer
CommonGuideLayer refers to a single layer. EasyGuide supports adding multiple layers by calling addLayer() more than once. Meanwhile，CommonGuideLayer also supports adding multiple highlights by calling addHighlightTarget() more than once. It can change or dismiss layer after clicking anywhere on it by default. If you want to control that action completely, you can do like this as below:
```
CommonGuideLayer layer = new CommonGuideLayer(context);
...
layer.setOnLayerClickListener(new CommonGuideLayer.OnLayerClickListener() {
    @Override
    public void onClick(int targetIndex, @NonNull ILayerController controller) {
        // If targetIndex < 0, clicking outside targets area;
        // If targetIndex >= 0, clicking inside targets area and targetIndex is the index of target that is clicked
        // controller.goNext(); //Dismiss the current layer and show the next layer if it exists
        // controller.dismiss(); //Dismiss the all layers
    }
});
```
Customize the shape of highlight area：
```
layer.setOnHighLightDrawListener(new CommonGuideLayer.OnHighLightDrawListener() {
    @Override
    public boolean onDraw(int index, @NonNull Rect rect, @NonNull Canvas canvas, @NonNull Paint paint) {
        // canvas.drawCircle(cx, cy, radius, paint);
        // canvas.drawRoundRect(new RectF(rect), r, r, paint);
        ...
    }
});
// layer.setOnHighLightDrawListener(null); //Cancel highlight
```
Add extra custom views such as a group of images and texts：
```
layer.addHighlightTarget(yourHighlightView).
    withExtraView(customView1, 0, 0, Location.TO_TOP).
    withExtraView(customView2, 0, 50, Location.TO_BOTTOM);
```
Other methods in CommonGuideLayer include：
 - setEnterAnimation;
 - setExitAnimation;
 - setBackgroundColor;
 - setOnDismissListener;
 - setOnShowListener;
## License
EasyGuide is released under the [Apache License Version 2.0](LICENSE).