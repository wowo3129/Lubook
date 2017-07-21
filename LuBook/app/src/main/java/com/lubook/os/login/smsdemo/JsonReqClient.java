package com.lubook.os.login.smsdemo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class JsonReqClient extends AbsRestClient {

    private static String dateToStr() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(new Date());
    }

    /**
     *   发送短信验证码接口
     * @param appid appID 短信平台 SMS_APPID
     * @param accountSid  短信平台 Account sid
     * @param token  短信平台 Auth Token
     * @param code 验证码
     * @param toPhone 手机号码
     * @param templateId 短信模板ID SMS_TEMPLATEID
     * @return
     */
    public String sendVerificationCode(String appid, String accountSid, String token, String code, String toPhone,String templateId) {
        String result = "";
        DefaultHttpClient httpclient = getDefaultHttpClient();
        try {
            //MD5加密
            EncryptUtil encryptUtil = new EncryptUtil();
            // 构造请求URL内容
            String timestamp = dateToStr();// 时间戳
            String signature = getSignature(accountSid, token, timestamp, encryptUtil);
            String url = getStringBuffer().append("/").append(version)
                    .append("/Accounts/").append(accountSid)
                    .append("/Messages/templateSMS")
                    .append("?sig=").append(signature).toString();
            JSONObject object = new JSONObject();
            JSONObject json = new JSONObject();
            json.put("appId", appid);
            json.put("templateId", templateId);
            json.put("to", toPhone);
            json.put("param", code);
            object.put("templateSMS", json);
            HttpResponse response = post(accountSid, timestamp, url, httpclient, encryptUtil, object.toString());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //  关闭连接
            httpclient.getConnectionManager().shutdown();
        }
        return result;
    }
}
