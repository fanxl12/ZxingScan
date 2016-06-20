package com.fanxl.zxing;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;

import com.fanxl.zxing.camera.CameraManager;
import com.google.zxing.Result;

/**
 * Created by fanxl2 on 2016/6/20.
 */
public interface CaptureInter {

	void handleDecode(Result rawResult, Bundle bundle);

	void setResult(int resultCode, Intent data);

	void finish();

	Rect getCropRect();

	Handler getHandler();

	CameraManager getCameraManager();

}
