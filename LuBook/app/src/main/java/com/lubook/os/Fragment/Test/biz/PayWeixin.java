package com.lubook.os.Fragment.Test.biz;

import com.blankj.utilcode.util.LogUtils;
import com.lubook.os.Fragment.Test.EncryptUtil;
import com.lubook.os.Fragment.Test.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZJcan on 2017-08-24.
 */

public class PayWeixin {
    private static String baseurl = "http://120.77.35.5:8001/index.php/intermgr/Wxpaydeal";
    private static String sign = "";

    public PayWeixin() {
        String s = "4C5A486D-8E0B-76FB-3AAA-1B571752D133";
        String strdate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String substring = EncryptUtil.MD5(strdate + s).substring(10, 30).toLowerCase();

        String s1 = EncryptUtil.MD5("20171013" + "4C5A486D-8E0B-76FB-3AAA-1B571752D133").substring(10, 30);
        //  6bd6630bfe088e42df2d

        LogUtils.d("main", "\"微信pay sign: " + s1 + "\tsubstring.legth== > " + substring.length());
        sign = substring;
    }

    public void GetPayQrcode(BeanWeixin weixin, IPayCallback callback) {
        if (weixin == null) return;
        String api = "/GetPayQrcode";
        String url = baseurl + api;
        Map<String, String> params = new HashMap<String, String>();
        params.put("total_fee", "" + weixin.getTotal_fee());//总费用
        params.put("body", weixin.getBody());
        params.put("attach", weixin.getAttach());
        params.put("goods_tag", weixin.getGoods_tag());
        params.put("product_id", weixin.getProduct_id());
        params.put("deviceNum", weixin.getDeviceNum());

        params.put("sign", sign);
        String result = HttpUtil.submitPostData(url, params, "utf-8");
        if (callback != null) {
            if (result.startsWith("error")) {
                callback.onError(result, "IOException or Http Request Fail");
            } else {
                callback.onResult(result);
            }
        }
    }

    /**
     * 主动查询
     *
     * @param weixin
     * @param callback
     */
    public static void QueryOrder(BeanWeixin weixin, IPayCallback callback) {
        if (weixin == null) return;
        String api = "/QueryOrder";
        String url = baseurl + api;
        Map<String, String> params = new HashMap<String, String>();
        params.put("out_trade_no", weixin.getOut_trade_no());
        params.put("deviceNum", weixin.getDeviceNum());
        params.put("sign", sign);
        String result = HttpUtil.submitPostData(url, params, "utf-8");
        if (callback != null) {
            if (result.startsWith("error")) {
                callback.onError(result, "IOException or Http Request Fail");
            } else {
                callback.onResult(result);
            }
        }
    }

    /**
     * 关闭订单
     *
     * @param weixin
     * @param callback
     */
    public static void CloseOrder(BeanWeixin weixin, IPayCallback callback) {
        if (weixin == null) return;
        String api = "/CloseOrder";
        String url = baseurl + api;
        Map<String, String> params = new HashMap<String, String>();
        params.put("out_trade_no", weixin.getOut_trade_no());
        params.put("deviceNum", weixin.getDeviceNum());
        params.put("sign", sign);
        String result = HttpUtil.submitPostData(url, params, "utf-8");
        if (callback != null) {
            if (result.startsWith("error")) {
                callback.onError(result, "IOException or Http Request Fail");
            } else {
                callback.onResult(result);
            }
        }
    }
}
