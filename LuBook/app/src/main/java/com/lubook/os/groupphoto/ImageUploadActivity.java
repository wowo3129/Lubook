package com.lubook.os.groupphoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.lubook.os.R;
import com.lubook.os.base.BaseActivity;
import com.lubook.os.zxing.QRCodeUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUploadActivity extends BaseActivity implements SurfaceHolder.Callback {
    private static String TAG = "main::upload";
    private final static String ACCESS_KEY = "h8t8zgMhQQVHJX4fvbzouQOyCqMCeBpe456KJApP";
    private final static String SECRET_KEY = "L2WcrJPua9owNLPZ1wkUVfLZU5ribQLeL0KTWqXS";
    private final static String bucketName = "reeman1";
    private String imageUil = "http://qn-dong.xfcy.me/";
    private String strCaptureFilePath = Environment.getExternalStorageDirectory() + File.separator + "QrCodeImage" + File.separator;
    private String photonamekey = "";
    private ImageView qrcode_image;
    private ImageView take_picture;
    private SurfaceView mSurfaceView;
    private Camera mCamera = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        initView();

    }

    private void initView() {
        qrcode_image = (ImageView) findViewById(R.id.qrcode_image);
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        take_picture = (ImageView) findViewById(R.id.take_picture);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(this);
        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    /**
     * 获取图片上传认证token
     */
    private void getUploadToken(final String filepath) {
        new Thread() {
            @Override
            public void run() {
                Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
                String token = auth.uploadToken(bucketName, null);
                if (token.trim().length() > 0 && (!token.equals(""))) {
                    uploadImageToQiniu(filepath, token);
                }
            }
        }.start();
    }

    /**
     * 上传图片到七牛
     *
     * @param filePath 要上传的图片路径
     * @param token    在七牛官网上注册的token
     */
    private void uploadImageToQiniu(String filePath, String token) {

        Configuration cfg = new Configuration(Zone.autoZone());
        UploadManager uploadManager = new UploadManager(cfg);
        photonamekey = "icon_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        try {
            Response response = uploadManager.put(filePath, photonamekey, token);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);/*解析上传成功的结果*/
            Log.d(TAG, "putRet.key:" + putRet.key + "\t" + "putRet.hash:" + putRet.hash);
            if (photonamekey.equals(putRet.key)) {
                imageUil = imageUil + putRet.key;
                Log.d(TAG, "上传成功，生成的图片外链：" + imageUil);
                String localqrcodepath = Environment.getExternalStorageDirectory() + File.separator + photonamekey;
                FileUtils.createOrExistsFile(localqrcodepath);
                createAndShowQr(imageUil, localqrcodepath);
            }

        } catch (QiniuException e) {
            Response r = e.response;
            try {
                LogUtils.e(TAG, "uploadImageToQiniu QiniuException:\n" + r.bodyString());
            } catch (QiniuException e1) {
                e1.printStackTrace();
            }
        }
    }


    private void createAndShowQr(final String imagepath, final String localqrcodepath) {


        new Thread(new Runnable() {
            public void run() {
                boolean success = QRCodeUtil.createQRImage(imagepath.toString().trim(), 400, 400,
                        BitmapFactory.decodeResource(getResources(), R.drawable.robot_head), localqrcodepath);
                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Bitmap bit = BitmapFactory.decodeFile(localqrcodepath);
                                qrcode_image.setImageBitmap(bit);
                                mSurfaceView.setVisibility(View.GONE);
                                qrcode_image.setVisibility(View.VISIBLE);
                                take_picture.setVisibility(View.GONE);
                            } catch (Exception e) {
                                LogUtils.e(TAG,"ErrorMsg:" +e.toString());
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 预览照片
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = null;
            mCamera = Camera.open(getDefaultCameraId());
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(1920, 1080);
            mCamera.setDisplayOrientation(90);
            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(mSurfaceView.getHolder());
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
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
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopCamera();
    }

    private void initCamera() {
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPreviewSize(1920, 1080);
                parameters.setPictureSize(1920, 1080);
                mCamera.setParameters(parameters);
                mCamera.startPreview();
                mCamera.cancelAutoFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 得到默认相机的ID
     *
     * @return
     */
    private int getDefaultCameraId() {
        int defaultId = 0;
        int mNumberOfCameras = Camera.getNumberOfCameras();
        Log.v("CameraActivity", "camera num: " + mNumberOfCameras);
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < mNumberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                defaultId = i;
            }
        }
        return defaultId;
    }


    private void stopCamera() {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void takePicture() {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPictureSize(1920, 1080);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            mCamera.takePicture(null, null, jpegCallback);
        }
    }

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] _data, Camera camera) {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(_data, 0, _data.length);
                String localPhotoName = "icon_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                File myCaptureFile = new File(strCaptureFilePath, localPhotoName);
                ImageUtils.save(bitmap, myCaptureFile, Bitmap.CompressFormat.JPEG, true);/*源文件，要保存到的文件，文件格式，回收*/
                stopCamera();

                getUploadToken(myCaptureFile.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

}
