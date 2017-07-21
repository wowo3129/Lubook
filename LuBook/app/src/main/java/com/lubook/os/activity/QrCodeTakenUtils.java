package com.lubook.os.activity;

import com.blankj.utilcode.util.LogUtils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class QrCodeTakenUtils {
    private final static String TAG = "main::CosQrCodeActivity";
    public final static String appid = "1252320671";
    public final static String BucketName = "reeman2";
    public final static String SecretId = "AKIDBAuObY4fpiKglZ4OQFqEONBl8HCwYmrl";
    public final static String SecretKey = "qTvAC0aw4zg9OQEoAIDyVotkNmxud9RV";
    public final static String HMAC_ALGORITHM = "HmacSHA1";
    public final static String CONTENT_CHARSET = "UTF-8";


    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    /**
     * @return 返回签名文件
     */
    public static String GetUploadSignature() {
        String strSign = "";
        String contextStr = "";

        long NowTime = System.currentTimeMillis() / 1000;
        long SignValidDuration = 3600 * 24 * 30;
        long endTime = NowTime + SignValidDuration;
        int iRandom = new Random().nextInt(Integer.MAX_VALUE);

        try {
            contextStr = "a=" + appid + "&b=" + BucketName + "&k=" + SecretId + "&e=" + endTime + "&t=" + NowTime + "&r=" + iRandom;
            LogUtils.d(TAG,"str000:"+contextStr);
            byte[] hash = HMAC_SHA(contextStr);

            byte[] sigBuf = byteMerger(hash, contextStr.getBytes("utf8"));
            strSign = new String(new BASE64Encoder().encode(sigBuf).getBytes());
            strSign = strSign.replace(" ", "").replace("\n", "").replace("\r", "");

            LogUtils.d(TAG,"\nstr111:"+strSign);

            byte[] bytes = new BASE64Decoder().decodeBuffer(strSign);
            LogUtils.d(TAG, "\nstr2222:" + new String(bytes));

        } catch (Exception e) {
            System.out.print(e.toString());
            return "";
        }
        return strSign;
    }

    private static byte[] HMAC_SHA(String contextStr) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec secretKey = new SecretKeySpec(SecretKey.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(contextStr.getBytes(CONTENT_CHARSET));
    }

}
