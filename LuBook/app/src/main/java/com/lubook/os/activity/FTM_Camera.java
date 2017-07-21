package com.lubook.os.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.blankj.utilcode.util.ImageUtils;
import com.lubook.os.R;

public class FTM_Camera extends Activity implements SurfaceHolder.Callback {

    private final static String TAG = "main::FTM_Camera";
    private int mCameraId = 0;
    private Camera mCamera; //Camera object
    private SurfaceView mSurfaceView; //surfaceView object
    private SurfaceHolder mSurfaceHolder; //SurfaceView holder
    private String strCaptureFilePath = Environment.getExternalStorageDirectory() + "/QrCodeImage/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ftm_camera_main);
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceholder) {
        try {
            mCamera = null;
            mCamera = Camera.open(mCameraId);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (Exception exception) {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceholder, int format, int w, int h) {
        //实现自动对焦
        if (mCamera != null) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        initCamera();//实现相机的参数初始化
                        camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
                    }
                }

            });
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        stopCamera();
    }

    private void takePicture() {
        if (mCamera != null) {
            Parameters parameters = mCamera.getParameters();
            parameters.setPictureSize(1920, 1080);
            parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
        }
    }

    private ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
        }
    };

    private PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] _data, Camera _camera) {
        }
    };

    private PictureCallback jpegCallback = new PictureCallback() {

        public void onPictureTaken(byte[] _data, Camera _camera) {
            try {
                Bitmap bm = BitmapFactory.decodeByteArray(_data, 0, _data.length);
                String localPhotoName = "icon_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()+".jpg");
                File myCaptureFile = new File(strCaptureFilePath, localPhotoName);
                ImageUtils.save(bm, myCaptureFile, Bitmap.CompressFormat.JPEG, true);/*源文件，要保存到的文件，文件格式，回收*/
                stopCamera();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void initCamera() {

        if (mCamera != null) {
            try {
                Parameters parameters = mCamera.getParameters();
                parameters.setPreviewSize(1920, 1080);
                mCamera.setParameters(parameters);
                mCamera.startPreview();
                mCamera.cancelAutoFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* 
     * Stop camera
     * */
    private void stopCamera() {

        if (mCamera != null) {
            try {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}