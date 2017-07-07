package com.lubook.os.zxing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.lubook.os.R;
import java.io.File;
import java.io.IOException;

public class MyQrActivity extends Activity {

    private TextView tip;
    private ImageView qr;
    private String text;// 二维码生成需要的文本
    private String filePath;// 二维码保存位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);
        tip = (TextView) findViewById(R.id.tip);
        qr = (ImageView) findViewById(R.id.qr);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void createAndShowQr() {

            filePath = "/sdcard/reeman/qr/myQr.jpg";
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
                            BitmapFactory.decodeResource(getResources(), R.drawable.login_reback), filePath);

                    if (success) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Bitmap bit = BitmapFactory.decodeFile(filePath);
                                    qr.setImageBitmap(bit);
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
