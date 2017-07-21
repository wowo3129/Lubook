package com.lubook.os.login.smsCheck.tools;

import java.security.MessageDigest;

public class MD5Tools {

	/**
	 * MD5数字签名
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String md5Digest(String src) throws Exception {
		// 定义数字签名方法, 可用：MD5, SHA-1
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] b = md.digest(src.getBytes("utf-8"));
		return byte2HexStr(b);
	}
	/**
	 * 字节数组转化为大写16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2HexStr(byte[] b) {
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
	 * BASE64编码
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String base64Encoder(String src) throws Exception {
		return new String(Base64.encode(src.getBytes("UTF-8")));
	}

	/**
	 * BASE64解码
	 * 
	 * @param dest
	 * @return
	 * @throws Exception
	 */
	public static String base64Decoder(String dest) throws Exception {
		return new String(Base64.decode(dest));
	}


}
