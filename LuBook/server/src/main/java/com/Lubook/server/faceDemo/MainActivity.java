package com.Lubook.server.faceDemo;


import com.baidu.aip.face.AipFace;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 百度AI
 */
public class MainActivity {
    //设置APPID/AK/SK
    public static final String APP_ID = "9879031";
    public static final String API_KEY = "YB7BqiAUuHYp5zdX3hIAWQ5C";
    public static final String SECRET_KEY = "qXMhhbs415v8IlI1pycSsS54HukHXhrv";
    private static final String TAG = "main::MainActivity";


    public static void main(String[] args) {

        // 初始化一个FaceClient
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        System.out.print(TAG + "0000000" + System.currentTimeMillis());

        humanFaceTest(client);
//        faceCompare(client);
    }


    private static void humanFaceTest(final AipFace client) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                // 自定义参数定义
                HashMap<String, String> options = new HashMap<String, String>();
                options.put("max_face_num", "1");
                options.put("face_fields", "beauty");

                // 参数为本地图片路径
                String imagePath = "sdcard/img/1.jpg";

                JSONObject response = client.detect(imagePath, options);

                long end = System.currentTimeMillis();
                System.out.print(TAG + response.toString() + "\n" + (end - start) + "毫秒");
            }
        }).start();

    }

    public static void faceCompare(final AipFace client) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                // 参数为本地图片路径 比较
                String imagePath1 = "sdcard/img/1.jpg";
                String imagePath2 = "sdcard/img/2.jpg";
                String imagePath3 = "sdcard/img/3.jpg";
                ArrayList<String> pathArray = new ArrayList<String>();
                pathArray.add(imagePath1);
                pathArray.add(imagePath2);
                pathArray.add(imagePath3);

                HashMap<String, String> options = new HashMap<>();
                options.put("image_liveness", "faceliveness,faceliveness,faceliveness");
//                options.put("ext_fields","qualities");
                JSONObject response = client.match(pathArray, options);

                long end = System.currentTimeMillis();
                System.out.print(TAG + response.toString() + "\n" + (end - start) + "毫秒");

            }
        }).start();

    }

}



