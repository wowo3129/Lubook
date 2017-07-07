package com.lubook.os.activity;

import android.os.Bundle;
import android.util.Log;

import com.lubook.os.R;
import com.lubook.os.base.BaseActivity;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Encoder;

public class QrCodeTakenUtils extends BaseActivity {
    private final static String TAG="main::CosQrCodeActivity";
    public  final static String appid = "1252320671";
    public final static String BucketName = "reeman2";
    public final static String SecretId = "AKIDBAuObY4fpiKglZ4OQFqEONBl8HCwYmrl";
    public final static String SecretKey = "qTvAC0aw4zg9OQEoAIDyVotkNmxud9RV";
    public final static String HMAC_ALGORITHM = "HmacSHA1";
    public final static String CONTENT_CHARSET = "UTF-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cos_qr_code);
        String s = GetUploadSignature();
        Log.d(TAG,"s:"+s);
    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    /**
     * @return 返回签名文件
     */
   public  static String GetUploadSignature() {
        String strSign = "";
        String contextStr = "";

        long NowTime = System.currentTimeMillis() / 1000;
        long SignValidDuration = 3600 * 24 * 30;
        long endTime = NowTime + SignValidDuration;
        int iRandom = new Random().nextInt(Integer.MAX_VALUE);

        try {
            contextStr =""
                    + "a="  + appid
                    + "&b=" + BucketName
                    + "&k=" + java.net.URLEncoder.encode(SecretId ,"utf8")
                    + "&t=" + NowTime
                    + "&e=" + endTime
                    + "&r=" + iRandom
                    /*+ "&f="*/;
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(SecretKey.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
            mac.init(secretKey);
            byte[] hash = mac.doFinal(contextStr.getBytes(CONTENT_CHARSET));
            byte[] sigBuf = byteMerger(hash, contextStr.getBytes("utf8"));
            strSign = new String(new BASE64Encoder().encode(sigBuf).getBytes());
            strSign = strSign.replace(" ", "").replace("\n", "").replace("\r", "");
        } catch (Exception e) {
            System.out.print(e.toString());
            return "";
        }
        return strSign;
    }
}
