package com.lubook.os.Fragment.Test;


import com.blankj.utilcode.util.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by luke on 2017/8/31.
 */

public class HttpUtil {
    private static final String TAG = HttpUtil.class.getSimpleName();
    public static final String BASE_URL = "http://120.77.35.5:8001/index.php/hotelmgr/Interfacefordevice/ServiceForDevice";
    private static HttpUtil instance;

    private OkHttpClient client;

    public HttpUtil() {
        client = new OkHttpClient.Builder().readTimeout(8000, TimeUnit.MILLISECONDS)
                .connectTimeout(8000, TimeUnit.MILLISECONDS).build();
    }

    public static HttpUtil get() {
        if (instance == null) {
            synchronized (HttpUtil.class) {
                instance = new HttpUtil();
            }
        }
        return instance;
    }


    public void get(Object tag, String url, Map<String, String> params, Callback callback) {


    }


    public void post(Object tag, String url, Map<String, String> params, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();

        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request;
        request = new Request.Builder()
                .tag(tag)
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void postHotel(Object tag, String url, String json, Callback callback){
        Request request;
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        LogUtils.d("main::postHotel------------------start-----------------","JSON:"+json);
        RequestBody body = RequestBody.create(JSON, json);
        request = new Request.Builder()
                .tag(tag)
                .post(body)
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }


//    String json = "{\"head\": {\"sign\": \"1940c5ee7c259dc52e128504d4e6fb5b\", \"sysCode\": " +
//            "\"SVCE0001\",  \"deviceNum\": \"SKW1coykzEgZfoWs\", \"transactionId\": " +
//            "\"S10262A010041705161150db9a002657d44b3591d427ae6529f707\", \"reqTime\": " +
//            "\"2017-05-16 11:50:13\"}, \"biz\": {\"HotelsID\": \"479\",\"OnlyRemain\":\"1\"," +
//            "\"RoomTypeCode\":\"CK\"}}";


    /*
     * Function  :   发送Post请求到服务器
     * Param     :   params请求体内容，encode编码格式
     */
    public static String submitPostData(String strUrlPath, Map<String, String> params, String encode) {

        byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
        try {

            URL url = new URL(strUrlPath);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);     //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            } else {
                return "error: " + response;
            }
        } catch (IOException e) {
            return "error: " + "0";
        }
    }

    /*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /*
     * Function  :   处理服务器的响应结果（将输入流转化成字符串）
     * Param     :   inputStream服务器的响应输入流
     */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = "error: 1";      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            return resultData;
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

}
