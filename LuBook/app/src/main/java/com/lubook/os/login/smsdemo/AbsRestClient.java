package com.lubook.os.login.smsdemo;

import java.io.ByteArrayInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class AbsRestClient {

    public static String version = "2014-06-30";

    public StringBuffer getStringBuffer() {
        StringBuffer sb = new StringBuffer("");
        //开发者使用
        sb.append("https://api.ucpaas.com:443");//.append("/rest");// /impl
        return sb;
    }

    public DefaultHttpClient getDefaultHttpClient() {
        //开发者使用
        DefaultHttpClient httpclient = new DefaultHttpClient();
        return httpclient;
    }

    public String getSignature(String accountSid, String authToken,
                               String timestamp, EncryptUtil encryptUtil) throws Exception {
        String sig = accountSid + authToken + timestamp;
        String signature = encryptUtil.md5Digest(sig);
        return signature;
    }

    public HttpResponse post(String accountSid, String timestamp,
                             String url, DefaultHttpClient httpclient, EncryptUtil encryptUtil,
                             String body) throws Exception {

        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Accept", "application/json");
        httppost.setHeader("Content-Type", "application/json;charset=utf-8");
        String src = accountSid + ":" + timestamp;
        String auth = encryptUtil.base64Encoder(src);
        httppost.setHeader("Authorization", auth);
        if (body != null && body.length() > 0) {
            BasicHttpEntity requestBody = new BasicHttpEntity();
            requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
            requestBody.setContentLength(body.getBytes("UTF-8").length);
            httppost.setEntity(requestBody);
        }
        // 执行客户端请求
        return httpclient.execute(httppost);
    }
}
