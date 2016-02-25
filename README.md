# ZxingScan
A Android library, you can scan the code bar code and two-dimensional code.  
一个Android Library, 集成进去可以实现一维码和二维码的扫码  
项目集成：
在项目的build.gradle文件中添加如下配置
repositories {
    maven { url "https://dl.bintray.com/fanxl12/maven/" }
}
dependencies {
    compile 'com.fanxl:zxing:1.0.0'
}
代码：
Intent scan = new Intent(this, CaptureActivity.class);
startActivityForResult(scan, 100);
扫码完成之后：
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

