package com.lubook.os.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lubook.os.R;
import com.lubook.os.zxing.QRCodeUtil;
import com.tencent.cos.COSClient;
import com.tencent.cos.COSConfig;
import com.tencent.cos.common.COSEndPoint;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadQrCodeActivity extends AppCompatActivity {
    private static String TAG="main::UploadImage";
    private COSClient cos;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_qr_code);

        context = getApplicationContext();

        String appid = QrCodeTakenUtils.appid;
        String peristenceId = null;
        COSConfig config = new COSConfig();/*//创建COSClientConfig对象，根据需要修改默认的配置参数*/
        config.setEndPoint(COSEndPoint.COS_GZ);/*//如设置园区*/
        config.setMaxRetryCount(3);
        cos = new COSClient(context, appid, config, peristenceId);


        String bucket = QrCodeTakenUtils.BucketName;
        String sign = QrCodeTakenUtils.GetUploadSignature();/*签名*/
        Log.d(TAG,"sign:"+sign+"\n");
        String cosPath = "image/";/*服务器图片的路径*/
        String imageName = "icon_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        final String srcPath = Environment.getExternalStorageDirectory() +"/1.jpg";/*待上传的本地的图片本地路径*/
        final String qrcodeLocalPath = "/sdcard/qrcodeImage/"+ imageName;/*生成的本地二维码的路径*/

        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket(bucket);
        putObjectRequest.setCosPath(cosPath);
        putObjectRequest.setSrcPath(srcPath);
        putObjectRequest.setSign(sign);
        putObjectRequest.setListener(new IUploadTaskListener() {

            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                final PutObjectResult result = (PutObjectResult) cosResult;
                if (result != null) {
                    createAndShowQr(qrcodeLocalPath, result.access_url);
                    Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                Log.w("TEST", "上传出错： ret =" + cosResult.code + "; msg =" + cosResult.msg+" \t"+cosResult.toString());
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

    private void createAndShowQr(final String filePath, final String text) {

        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        new Thread(new Runnable() {
            public void run() {
                boolean success = QRCodeUtil.createQRImage(text.toString().trim(), 400, 400,
                        BitmapFactory.decodeResource(getResources(), R.drawable.music_round), filePath);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Bitmap bit = BitmapFactory.decodeFile(filePath);
//                                taken_qrcode_image.setImageBitmap(bit);
                            } catch (Exception e) {
                                System.out.println("QrCodeActivity : Exception");
                            }
                        }
                    });
                }
            }
        }).start();
    }

}
