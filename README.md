# ZxingScan
A Android library, you can scan the code bar code and two-dimensional code.  

一个Android Library, 集成进去可以实现一维码和二维码的扫码 

## 效果图

<img src="Images\zxing_01.jpg" width="400px"/> <img src="Images\zxing_02.jpg" width="400px"/>

## 项目集成：

* Android Studio

在项目的build.gradle文件中添加如下配置
```xml
repositories {
    maven { url "https://dl.bintray.com/fanxl12/maven/" }
}
```
然后加入
```
dependencies {
    compile 'com.fanxl:zxing:1.0.0'
}
```

## 使用代码：

到扫码界面，只需要跳转过去就可以
```
Intent scan = new Intent(this, CaptureActivity.class);
startActivityForResult(scan, 100);
```

扫码完成之后：
```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode == CaptureActivity.SCAN_RESULT_CODE){
        Bundle extras = data.getExtras();
        if(extras!=null){
            //扫码结果
            String scanResult = extras.getString("result");
            
        }
    }
}
```
这样就可以获取扫码的结果。

最后不要忘记，需要在工程的AndroidManifest.xml中添加权限和声明：
```xml
<activity android:name="com.fanxl.zxing.CaptureActivity">
</activity>
```

