package com.lubook.os.login.smsCheck.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetWorkTools {

	/** * 无网络连接 */
	public static final int NETWORK_NONE = 0;
	/** * 2G信号(包含:2.75G  2.5G 2G) */
	public static final int NETWORK_2G = 1;
	/** * 3G信号(包含:3G  3.5G  3.75G) */
	public static final int NETWORK_3G = 2;
	/** * 4G信号 */
	public static final int NETWORK_4G = 3;
	/** * WIFI信号 */
	public static final int NETWORK_WIFI = 4;
	/** * 以太网 */
	public static final int NETWORK_ETHERNET = 5;

	private static NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo;
	}

	/**
	 * 当前是否有网络连接
	 * 
	 * @param context
	 * @return true，false
	 */
	public static boolean isNetWorkConnected(Context context) {
		NetworkInfo networkInfo = getNetworkInfo(context);
		return networkInfo != null && networkInfo.isConnected();
	}

	public static int getCurrentNetWorkSate(Context context) {
		int state = NETWORK_NONE;
		NetworkInfo networkInfo = getNetworkInfo(context);
		if(networkInfo!=null){
			String typeName = networkInfo.getTypeName();
			int subtype = networkInfo.getSubtype();
			if("WIFI".equalsIgnoreCase(typeName)){
				state = NETWORK_WIFI;
			}else {
				if(typeName!=null && typeName.toLowerCase().contains("mobile")){
					if(subtype == TelephonyManager.NETWORK_TYPE_UMTS
						|| subtype == TelephonyManager.NETWORK_TYPE_EVDO_0
						|| subtype == TelephonyManager.NETWORK_TYPE_EVDO_A
						|| subtype == TelephonyManager.NETWORK_TYPE_EVDO_B
						|| subtype == TelephonyManager.NETWORK_TYPE_EHRPD
						|| subtype == TelephonyManager.NETWORK_TYPE_HSDPA
						|| subtype == TelephonyManager.NETWORK_TYPE_HSUPA
						|| subtype == TelephonyManager.NETWORK_TYPE_HSPA
						|| subtype == TelephonyManager.NETWORK_TYPE_LTE
						// 4.0系统 H+网络为15 TelephonyManager.NETWORK_TYPE_HSPAP
						|| subtype == 15){
						state = NETWORK_3G;
					}else if(networkInfo.getTypeName() != null && networkInfo.getTypeName().toUpperCase().contains("ETHERNET")){
						state = NETWORK_2G;
					}else {
						state = NETWORK_ETHERNET;
					}
				}
			}
		}
		return state;
	}
}
