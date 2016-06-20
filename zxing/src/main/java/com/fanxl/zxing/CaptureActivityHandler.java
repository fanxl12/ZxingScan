/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fanxl.zxing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.fanxl.zxing.camera.CameraManager;
import com.fanxl.zxing.decode.DecodeHandler;
import com.fanxl.zxing.decode.DecodeThread;
import com.google.zxing.Result;

/**
 * This class handles all the messaging which comprises the state machine for
 * capture.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public class CaptureActivityHandler extends Handler {

	private final CaptureInter activity;
	private final DecodeThread decodeThread;
	private final CameraManager cameraManager;
	private State state;
	public static final int RESTART_PREVIEW = 100;
	public static final int DECODE_SUCCEEDED = 101;
	public static final int DECODE_FAILED = 102;
	public static final int RETURN_SCAN_RESULT = 103;

	private enum State {
		PREVIEW, SUCCESS, DONE
	}

	public CaptureActivityHandler(CaptureInter activity, CameraManager cameraManager, int decodeMode) {
		this.activity = activity;
		decodeThread = new DecodeThread(activity, decodeMode);
		decodeThread.start();
		state = State.SUCCESS;

		// Start ourselves capturing previews and decoding.
		this.cameraManager = cameraManager;
		cameraManager.startPreview();
		restartPreviewAndDecode();
	}

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case RESTART_PREVIEW:
			restartPreviewAndDecode();
			break;
		case DECODE_SUCCEEDED:
			state = State.SUCCESS;
			Bundle bundle = message.getData();

			activity.handleDecode((Result) message.obj, bundle);
			break;
		case DECODE_FAILED:
			// We're decoding as fast as possible, so when one decode fails,
			// start another.
			state = State.PREVIEW;
			cameraManager.requestPreviewFrame(decodeThread.getHandler(), DecodeHandler.DECODE);
			break;
		case RETURN_SCAN_RESULT:
			activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
			activity.finish();
			break;
		}
	}

	public void quitSynchronously() {
		state = State.DONE;
		cameraManager.stopPreview();
		Message quit = Message.obtain(decodeThread.getHandler(), DecodeHandler.QUIT);
		quit.sendToTarget();
		try {
			// Wait at most half a second; should be enough time, and onPause()
			// will timeout quickly
			decodeThread.join(500L);
		} catch (InterruptedException e) {
			// continue
		}

		// Be absolutely sure we don't send any queued up messages
		removeMessages(CaptureActivityHandler.DECODE_SUCCEEDED);
		removeMessages(CaptureActivityHandler.DECODE_FAILED);
	}

	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			cameraManager.requestPreviewFrame(decodeThread.getHandler(), DecodeHandler.DECODE);
		}
	}

}
