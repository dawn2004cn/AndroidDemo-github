/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.noahedu.gpuimage.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.view.Surface;

import com.noahedu.common.filedownloader.base.Log;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

public class CameraHelper {
    final public static String TAG = CameraHelper.class.getSimpleName();
    private final CameraHelperImpl mImpl;

    public CameraHelper(final Context context) {
        if (SDK_INT >= GINGERBREAD) {
            mImpl = new CameraHelperGB();
        } else {
            mImpl = new CameraHelperBase(context);
        }
    }

    public interface CameraHelperImpl {
        int getNumberOfCameras();

        Camera openCamera(int id);

        Camera openDefaultCamera();

        Camera openCameraFacing(int facing);

        boolean hasCamera(int cameraFacingFront);

        void getCameraInfo(int cameraId, CameraInfo2 cameraInfo);
    }

    public int getNumberOfCameras() {
        return mImpl.getNumberOfCameras();
    }

    public Camera openCamera(final int id) {
        return mImpl.openCamera(id);
    }

    public Camera openDefaultCamera() {
        return mImpl.openDefaultCamera();
    }

    public Camera openFrontCamera() {
        return mImpl.openCameraFacing(CameraInfo.CAMERA_FACING_FRONT);
    }

    public Camera openBackCamera() {
        return mImpl.openCameraFacing(CameraInfo.CAMERA_FACING_BACK);
    }

    public boolean hasFrontCamera() {
        return mImpl.hasCamera(CameraInfo.CAMERA_FACING_FRONT);
    }

    public boolean hasBackCamera() {
        return mImpl.hasCamera(CameraInfo.CAMERA_FACING_BACK);
    }

    public void getCameraInfo(final int cameraId, final CameraInfo2 cameraInfo) {
        mImpl.getCameraInfo(cameraId, cameraInfo);
    }

    public void setCameraDisplayOrientation(final Activity activity,
            final int cameraId, final Camera camera) {
        int result = getCameraDisplayOrientation(activity, cameraId);
        camera.setDisplayOrientation(result);
    }

    public int getCameraDisplayOrientation(final Activity activity, final int cameraId) {
        //获得当前屏幕方向
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            //若屏幕方向与水平轴负方向的夹角为0度
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            //若屏幕方向与水平轴负方向的夹角为90度
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            //若屏幕方向与水平轴负方向的夹角为180度
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            //若屏幕方向与水平轴负方向的夹角为270度
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        //通过相机ID获得相机信息
        CameraInfo2 info = new CameraInfo2();
        getCameraInfo(cameraId, info);
        Log.v(TAG,"CameraInfo2.info:"+info);
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            //前置摄像头作镜像翻转
            result = (info.orientation + degrees) % 360;
            //result = (360 - result) % 360;  // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        Log.v(TAG,"info.orientation:"+info.orientation+",degrees:"+degrees+",result:"+result+",rotation:"+rotation+",info.facing:"+info.facing);
        return result;
    }

    public static class CameraInfo2 {
        public int facing;
        public int orientation;
    }
}
