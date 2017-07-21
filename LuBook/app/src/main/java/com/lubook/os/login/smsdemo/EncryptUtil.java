/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.lubook.os.login.smsdemo;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class EncryptUtil {

    private static final String UTF8 = "utf-8";

    /**
     * MD5数字签名
     *
     * @param src
     * @return
     * @throws Exception
     */
    public String md5Digest(String src) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(src.getBytes(UTF8));
        return this.byte2HexStr(b);
    }

    /**
     * BASE64编码
     *
     * @param src
     * @return
     * @throws Exception
     */
    public String base64Encoder(String src) throws Exception {
        return new String(Base64.encode(src.getBytes(UTF8)));
    }

    /**
     * BASE64解码
     *
     * @param dest
     * @return
     * @throws Exception
     */
    public String base64Decoder(String dest) throws Exception {
        return new String(Base64.decode(dest));
    }

    /**
     * 字节数组转化为大写16进制字符串
     *
     * @param b
     * @return
     */
    private String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String s = Integer.toHexString(b[i] & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 解密是否成功的校验码
     */
    private static String CHECK_CODE = "http:\\wwww.Chemi.com";

    /**
     * 加密数据
     *
     * @param key       私钥
     * @param plaintext 需要加密的明文
     * @return ciphertext 加密后的密文
     */
    public static String Encrypt(String key, String plaintext) {
        if (TextUtils.isEmpty(plaintext)) {// 如果明文为空
            return "";
        }
        StringBuffer buf = new StringBuffer();

        key = CHECK_CODE + key;//  私钥加上校验位
        plaintext = CHECK_CODE + plaintext; // 明文加上效验位

        byte[] keys = md5Encrypt(key).getBytes(); // 32位MD5私钥
        for (int i = 0; i < plaintext.length(); i++) {
            buf.append((char) (plaintext.charAt(i) ^ keys[i % 32]));
        }

        String ciphertext = buf.toString();
        return ciphertext;

    }


    /**
     * 解密密文
     *
     * @param key        私钥
     * @param ciphertext 需要解密的密文
     * @return plaintext 解密后的明文
     */
    public static String DEcrypt(String key, String ciphertext) {
        if (TextUtils.isEmpty(ciphertext)) {// 如果密文为空
            return "";
        }
        StringBuffer buf = new StringBuffer();

        key = CHECK_CODE + key;//  私钥加上校验位
        byte[] keys = md5Encrypt(key).getBytes(); // 32位MD5私钥
        for (int i = 0; i < ciphertext.length(); i++) {
            buf.append((char) (ciphertext.charAt(i) ^ keys[i % 32]));
        }
        String plaintext = buf.toString(); // 解密后的明文
        if (!plaintext.startsWith(CHECK_CODE)) {// 解密失败
            plaintext = "";
        } else {
            plaintext = plaintext.substring(CHECK_CODE.length()); // 解密成功，去除校验位
        }
        return plaintext;

    }

    /**
     * 解密密文(使用MD5私钥解密)
     *
     * @param md5Key     md5私钥
     * @param ciphertext 需要解密的密文
     * @return plaintext 解密后的明文
     */
    public static String DEcryptForMd5Key(String md5Key, String ciphertext) {

        StringBuffer buf = new StringBuffer();

        byte[] keys = md5Key.getBytes(); // 32位MD5私钥
        for (int i = 0; i < ciphertext.length(); i++) {
            buf.append((char) (ciphertext.charAt(i) ^ keys[i % 32]));
        }
        String plaintext = buf.toString(); // 解密后的明文
        if (!plaintext.startsWith(CHECK_CODE)) {// 解密失败
            plaintext = "";
        } else {
            plaintext = plaintext.substring(CHECK_CODE.length()); // 解密成功，去除校验位
        }

        return plaintext;

    }

    /**
     * 获取MD5 串
     *
     * @param plaintext
     * @return
     */
    public static String getMD5Key(String plaintext) {
        return md5Encrypt(CHECK_CODE + plaintext);
    }

    /**
     * MD5加密
     *
     * @param plaintext 需要进行MD5加密的明文
     * @return ciphertext MD5加密后的密文
     */
    public static String md5Encrypt(String plaintext) {// 保持编码为UTF-8
        if (TextUtils.isEmpty(plaintext)) {
            plaintext = CHECK_CODE;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plaintext.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

}  
