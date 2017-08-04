package com.lubook.os.faceAi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lubook.os.R;
import com.lubook.os.base.BaseActivity;

import java.net.URLEncoder;

public class FaceMatchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_match);
        Button face_match = (Button) findViewById(R.id.face_match);
        face_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        main();

                    }
                }).start();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public void main() {
        long start = System.currentTimeMillis();
        // 人脸对比url
        String matchUrl = "https://aip.baidubce.com/rest/2.0/face/v2/match";
        // 本地文件路径，可用多张图片
        String filePath1 = "/sdcard/reeman/1.jpg";
        String filePath2 = "/sdcard/reeman/4.jpg";
        String filePath3 = "/sdcard/reeman/5.jpg";
        try {
            byte[] imgData1 = FileUtil.readFileByBytes(filePath1);
            byte[] imgData2 = FileUtil.readFileByBytes(filePath2);
            byte[] imgData3 = FileUtil.readFileByBytes(filePath3);
            String imgStr1 = Base64Util.encode(imgData1);
            String imgStr2 = Base64Util.encode(imgData2);
            String imgStr3 = Base64Util.encode(imgData3);
            String params = URLEncoder.encode("images", "UTF-8") + "="
                    + URLEncoder.encode(imgStr1 + "," + imgStr2 + "," + imgStr3, "UTF-8");
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
            String accessToken = AuthService.getAuth();
//            String accessToken = "#####调用鉴权接口获取的token#####";
            String result = HttpUtil.post(matchUrl, accessToken, params);
            System.out.println("main:1111" + result);
            long end = System.currentTimeMillis();
            System.out.print("main::1111--->"+(start - end)/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
