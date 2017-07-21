package com.jm;

import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import sun.misc.BASE64Encoder;
 class signs {
    public String m_strSecId;
    public String m_strSecKey;
    public long m_qwNowTime;
    public int m_iRandom;
    public int m_iSignValidDuration;

    private static final String HMAC_ALGORITHM = "HmacSHA1";
    private static final String CONTENT_CHARSET = "UTF-8";

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    String GetUploadSignature() {
        String strSign = "";
        String contextStr = "";
        long endTime = (m_qwNowTime + m_iSignValidDuration);
        try {
            contextStr += "secretId=" + java.net.URLEncoder.encode(this.m_strSecId, "utf8");
            contextStr += "&currentTimeStamp=" + this.m_qwNowTime;
            contextStr += "&expireTime=" + endTime;
            contextStr += "&random=" + this.m_iRandom;

            String s = contextStr;
            String sig = null;
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(m_strSecKey.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
            mac.init(secretKey);
            byte[] hash = mac.doFinal(contextStr.getBytes(CONTENT_CHARSET));
            byte[] sigBuf = byteMerger(hash, contextStr.getBytes("utf8"));
            strSign = new String(new BASE64Encoder().encode(sigBuf).getBytes());
            strSign = strSign.replace(" ", "").replace("\n", "").replace("\r", "");
            byte[] bytes = new BASE64Decoder().decodeBuffer(strSign);
            System.out.print("你::::\n"+new String(bytes));
        } catch (Exception e) {
            System.out.print(e.toString());
            return "";
        }
        return strSign;
    }
}

public class signtest {
    public static void main(String[] args) {
        signs sign = new signs();
        sign.m_strSecId = "个人API密钥中的Secret Id";
        sign.m_strSecKey = "个人API密钥中的Secret Key";
        sign.m_qwNowTime = System.currentTimeMillis() / 1000;
        sign.m_iRandom = new Random().nextInt(java.lang.Integer.MAX_VALUE);
        sign.m_iSignValidDuration = 3600 * 24 * 2;

        System.out.print(sign.GetUploadSignature());
    }
}