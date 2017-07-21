package com.lubook.os.login.smsCheck.dao;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.lubook.os.login.smsCheck.bean.PostBean;
import com.lubook.os.login.smsCheck.tools.MD5Tools;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RestDao {

	public static final String accountsid = "55252684882359b40e8a87151e07a061";
	public static final String authtoken = "9879029bc92488692f3f3e56dad64faf";
	public static final String resturl = "https://api.ucpaas.com";
	public static final String appId = "b5ff537edce84f53b1b0feb8c18a84e1";
	public static final String templeteId = "103000";
	public static final String version = "2014-06-30 ";

	/**
	 * <pre>
	 * POST/2014-06-30/Accounts/e03bc9106c6ed0eaebfce8c368fdcd48/Messages/templateSMS?sig=769190B9A223549407D2164CAE92152E
	 * /{SoftVersion}/Accounts/{accountSid}/Messages/templateSMS
	 * Host:api.ucpaas.com
	 * Accept:application/json
	 * Content-Type:application/json;charset=utf-8
	 * Authorization:ZTAzYmM5MTA2YzZlZDBlYWViZmNlOGMzNjhmZGNkNDg6MjAxNDA2MjMxODUwMjE=
	 * {
	 *  "templateSMS" : {
	 *     "appId"       : "e462aba25bc6498fa5ada7eefe1401b7",
	 *     "param"       : "0000",
	 *     "templateId"  : "1",
	 *     "to"          : "18612345678"
	 *     }
	 * }
	 * </pre>
	 */
	public static final String TIP = "TIP";

	/**
	 *
	 * @param client
	 *            httpClient
	 * @param uri
	 * @param body
	 * @return
	 * @throws Exception
	 */
	public HttpResponse post(DefaultHttpClient client, String uri, String body)
			throws Exception {
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
		String authorization = getAuthorization();
		httpPost.setHeader("Authorization", authorization);
		if (body != null && body.length() > 0) {
			BasicHttpEntity entity = new BasicHttpEntity();
			entity.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
			entity.setContentLength(body.getBytes().length);
			httpPost.setEntity(entity);
		}
		HttpResponse response = client.execute(httpPost);
		return response;

	}

	public String checkNum(String to) {
		String resp = "fuck me";
		setParam();
		DefaultHttpClient client = new DefaultHttpClient();
		String uri = getSmsRestUri();
		PostBean bean = new PostBean();
		bean.appId = appId;
		bean.param = getparam();
		Log.e("aaa", "param--"+param);
		bean.templateId = templeteId;
		bean.to = to;
		String body = new Gson().toJson(bean);
		body = "{\"templateSMS\":" + body + "}";
		try {
			Log.e("aaa", "yyyy--uri--" + uri);
			HttpResponse response = post(client, uri, body);
			int statusCode = response.getStatusLine().getStatusCode();
			Log.e("aaa", "post--stateCode--" + statusCode);
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				resp = EntityUtils.toString(entity, "utf-8");
			}
		} catch (Exception e) {
			Log.e("aaa", "abcd e:" + e);
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}

		return resp;
	}

	private String getparam() {
		return param;
	}

	private String param;

	private void setParam() {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			sb.append(random.nextInt(10));
		}
		sb.append(",").append("2");
		param = sb.toString();

	}

	/**
	 * * POST/2014-06-30/Accounts/e03bc9106c6ed0eaebfce8c368fdcd48/Messages
	 * /templateSMS?sig=769190B9A223549407D2164CAE92152E //
	 * /{SoftVersion}/Accounts/{accountSid}/Messages/templateSMS
	 *
	 * @return
	 */
	private String getSmsRestUri() {
		StringBuffer sb = new StringBuffer();
		String url = sb.append(resturl).append("/").append(version).append("/")
				.append("Accounts").append("/").append(accountsid)
				.append("/Messages/templateSMS").append("?sig=")
				.append(SigParameter).toString();
		return url;
	}

	/**
	 * <pre>
	 * 3. Authorization是包头验证信息
	 * ◾ 使用Base64编码（账户Id + 冒号 + 时间戳）
	 * ◾ 冒号为英文冒号
	 * ◾ 时间戳是当前系统时间（24小时制），格式“yyyyMMddHHmmss”，需与SigParameter中时间戳相同。
	 * </pre>
	 *
	 * @return
	 * @throws Exception
	 */
	private String getAuthorization() throws Exception {
		String src = accountsid + ":" + getTimeStamp();
		String authorization = MD5Tools.base64Encoder(src);

		return authorization;
	}

	// {SoftVersion}/Accounts/{accountSid}/{function}/{operation}?sig={SigParameter}

	// /{SoftVersion}/Accounts/{accountSid}/Messages/templateSMS

	String SigParameter = getSigParameter();

	/**
	 * <pre>
	 * 	 SigParameter是REST API 验证参数
	 * 	◾ URL后必须带有sig参数，sig= MD5（账户Id + 账户授权令牌 + 时间戳），共32位(注:转成大写)
	 * 	 ◾ 使用MD5加密（账户Id + 账户授权令牌 + 时间戳），共32位
	 * 	 ◾ 时间戳是当前系统时间（24小时制），格式“yyyyMMddHHmmss”。时间戳有效时间为50分钟。
	 * </pre>
	 *
	 * @return
	 */
	private String getSigParameter() {
		String timeStamp = getTimeStamp();
		String sig="";
		try {
			sig = MD5Tools.md5Digest(accountsid + authtoken + timeStamp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sig.toUpperCase();// 转大写
	}

	/**
	 * 时间戳是当前系统时间（24小时制），格式“yyyyMMddHHmmss”，需与SigParameter中时间戳相同。
	 *
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private String getTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(new Date());
		return date;

	}
}
