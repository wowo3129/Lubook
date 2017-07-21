package com.lubook.os.activity;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.PermissionUtils;
import com.lubook.os.R;
import com.lubook.os.base.BaseActivity;
import com.lubook.os.zxing.QRCodeUtil;
import com.tencent.cos.COSClient;
import com.tencent.cos.COSClientConfig;
import com.tencent.cos.COSConfig;
import com.tencent.cos.common.COSEndPoint;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Decoder.BASE64Decoder;

public class UploadQrCodeActivity extends BaseActivity implements PermissionUtils.OnPermissionListener {
    private static String TAG = "main::UploadImage";
    private COSClient cos;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_qr_code);
        context = getApplicationContext();
        PermissionUtils.requestPermissions(this, 1001, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, this);
    }


    @Override
    public void onPermissionGranted() {
        try {
            uploadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPermissionDenied(String[] deniedPermissions) {
        if (!PermissionUtils.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            try {
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void uploadImage() throws IOException {
        String appid = QrCodeTakenUtils.appid;
        String peristenceId = null;
        COSClientConfig config = new COSClientConfig();/*//创建COSClientConfig对象，根据需要修改默认的配置参数*/
        config.setEndPoint(COSEndPoint.COS_GZ);/*//如设置园区*/
        config.setMaxRetryCount(3);
        cos = new COSClient(context, appid, config, peristenceId);

        String sign = QrCodeTakenUtils.GetUploadSignature();/*签名*/

        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket("reeman2");
        putObjectRequest.setCosPath("image/");
        putObjectRequest.setSrcPath("sdcard/1.jpg");
        putObjectRequest.setSign(sign);
        putObjectRequest.setListener(new IUploadTaskListener() {

            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                final PutObjectResult result = (PutObjectResult) cosResult;
                if (result != null) {
                    Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
                    Log.w("TEST", "上传成功！");
                }
            }
            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                Log.w("TEST", "上传出错： ret =" + cosResult.code + "; msg =" + cosResult.msg + " \t" + cosResult.toString());
            }

            @Override
            public void onProgress(COSRequest cosRequest, long currentSize, long totalSize) {
                float progress = (float) currentSize / totalSize;
                progress = progress * 100;
                Log.w("TEST", "进度：  " + (int) progress + "%");
            }

            @Override
            public void onCancel(COSRequest cosRequest, COSResult cosResult) {

            }
        });
        PutObjectResult result = cos.putObject(putObjectRequest);
    }

}
