package com.lubook.os.Fragment.Test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lubook.os.Fragment.Test.biz.BeanWeixin;
import com.lubook.os.Fragment.Test.biz.IPayCallback;
import com.lubook.os.Fragment.Test.biz.PayWeixin;
import com.lubook.os.R;
import com.lubook.os.zxing.QRCodeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.lubook.os.R.id.qr;


public class FragmentTestActivity extends AppCompatActivity implements LeftFragment.OnFragmentInteractionListener, RightFragment.OnFragmentInteractionListener, ThreeFragment.OnFragmentInteractionListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction mTransaction;
    private RightFragment rightFragment;
    private ThreeFragment threeFragment;
    private ImageView imageView;
    private final static String RoomNo = "2312";
    private final static String HotelsID = "797";//测试的酒店ID 预定单：479  在住：797
    private static String GuestAccount = "";
    private ImageView qr_img;

    //3042
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
        initView();
    }

    private void initView() {
        //Fragment碎片初始化
        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(1);
            }
        });

        qr_img = (ImageView) findViewById(R.id.qr_img);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void setTabSelection(int index) {
        mTransaction = fragmentManager.beginTransaction();
        switch (index) {
            case 0:
                if (rightFragment == null) {
                    rightFragment = new RightFragment();
                    mTransaction.add(R.id.frame_content, rightFragment);
                }
                mTransaction.show(rightFragment);
                mTransaction.replace(R.id.frame_content, rightFragment);

                break;
            case 1:
                if (threeFragment == null) {
                    threeFragment = new ThreeFragment();
                    mTransaction.add(R.id.frame_content, threeFragment);
                }
                mTransaction.show(threeFragment);
                mTransaction.replace(R.id.frame_content, threeFragment);
                break;
            default:
                break;
        }
        mTransaction.commit();
    }


    /**
     * 查询房间 SVCE0001
     *
     * @param view
     */
    public void onTest(View view) {
        JSONObject root = new JSONObject();
        try {

            JSONObject head = new JSONObject();
            head.put("sysCode", "SVCE0001");//应用编码 查询信息，
            head.put("deviceNum", "SKW1coykzEgZfoWs");//设备应用编码 用AI的那个APPID web端看到
            String transactionId = getTransactionId();
            head.put("transactionId", transactionId);//"S10262A010041705161150db9a002657d44b3591d427ae6529f707"
            head.put("sign", getSign(transactionId));
            head.put("reqTime", getStringDate());//改为自己的格式 这个，传参数表示当前的房间情况 当前时间

            root.put("head", head);
            JSONObject biz = new JSONObject();
            biz.put("HotelsID", HotelsID); //酒店 ID 是固定的吗？发现任意数字都可以查出来房间 479
            biz.put("OnlyRemain", "0"); //仅 查 余 房 传 参 数，是1 /否0
            biz.put("VIPInfo", "");//
            biz.put("RoomTypeCode", "CK"); //CK 这个还有其他的类型吗？有。房间类型编码，可以传空，显示所有的信息
            root.put("biz", biz);

        } catch (Exception e) {

        }
        final String json = root.toString();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        HttpUtil.get().postHotel("hotel", HttpUtil.BASE_URL, json, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("main", "==============fail---");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.d("main", "==============response" + response.code());
                                LogUtils.d("main", "状态码: " + response.code() + "【1】查询房间: " + response.body().string());

                            }
                        });
                    }
                }

        ).start();

    }

    /**
     * 提交入住订单 1102  1107 1105  1106 1110 1111 1113 2002
     * SVCE0002
     */
    public void onBookingRoom(View view) {
        JSONObject root = new JSONObject();
        try {

            JSONObject head = new JSONObject();
            head.put("sysCode", "SVCE0002");//提交入住订单
            head.put("deviceNum", "SKW1coykzEgZfoWs");
            String transactionId = getTransactionId();
            head.put("transactionId", transactionId);//"S10262A010041705161150db9a002657d44b3591d427ae6529f707"
            head.put("sign", getSign(transactionId));
            head.put("reqTime", getStringDate());//改为自己的格式 这个，传参数表示当前的房间情况 当前时间

            root.put("head", head);

            JSONObject biz = new JSONObject();
            biz.put("HotelsID", HotelsID); //酒店 ID 是固定的吗？发现任意数字都可以查出来房间 479
            biz.put("RoomNo", RoomNo);//房号
            biz.put("GuestName", "菲菲");//客人姓名
            biz.put("PaidAmt",1f);//金額
            biz.put("GuestID", "610525199202022515");//"610525199202022515"
            biz.put("Gender", "男");//"男"

            biz.put("PhoneNumber", "17791043536");//暂时不用
            biz.put("Address", " 广东省深圳市南山区"); //暂时不用
            biz.put("Adults", "1");//人数 暂时不用
            biz.put("ArriveTime", "2017-10-11 18:00:00");//暂时不用
            biz.put("DepartureTime", "2017-10-12 18:00:00");//暂时不用

            root.put("biz", biz);

        } catch (Exception e) {

        }
        final String json = root.toString();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        HttpUtil.get().postHotel("hotel", HttpUtil.BASE_URL, json, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("main", "==============提交入住订单fail---");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String json = response.body().string();
                                LogUtils.d("main", "状态码" + response.code() + "【2】提交入住订单: " + json);
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(json);
                                    JSONObject headObj = jsonObject.optJSONObject("head");
                                    String code = headObj.optString("code");
                                    if (code.equals("0")) {
                                        JSONObject jsonObject1 = jsonObject.optJSONObject("biz");
                                        GuestAccount = jsonObject1.optString("GuestAccount");
                                        Log.d("main", "==============预定成功---：：：：" + GuestAccount);

                                    } else if (code.equals("1")) {
                                        String err = headObj.optString("err");
                                        Log.d("main::", "err: " + err);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                                3060
                            }
                        });
                    }
                }

        ).start();

    }

    /**
     * 订单入住确认 SVCE0006
     */
    public void onConfirmRoomOrder(View v) {
        JSONObject root = new JSONObject();
        try {

            JSONObject head = new JSONObject();
            head.put("sysCode", "SVCE0006");//应用编码 查询信息，
            head.put("deviceNum", "SKW1coykzEgZfoWs");//设备应用编码 用AI的那个APPID web端看到
            String transactionId = getTransactionId();
            head.put("transactionId", transactionId);//"S10262A010041705161150db9a002657d44b3591d427ae6529f707"
            head.put("sign", getSign(transactionId));
            head.put("reqTime", getStringDate());//改为自己的格式 这个，传参数表示当前的房间情况 当前时间

            root.put("head", head);
            JSONObject biz = new JSONObject();
            biz.put("HotelsID", HotelsID); //酒店ID
            biz.put("GuestAccount", GuestAccount);
            biz.put("CheckInConfirm", "1");//1 确认 0 撤单
            root.put("biz", biz);
        } catch (Exception e) {

        }
        final String json = root.toString();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        HttpUtil.get().postHotel("hotel", HttpUtil.BASE_URL, json, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("main", "==============订单入住确认fail---");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String json = response.body().string();
                                LogUtils.d("main", "状态码：" + response.code() + "\t【2】订单入住确认body: " + json);
                                try {
                                    JSONObject jsonobj = new JSONObject(json);
                                    JSONObject head = jsonobj.optJSONObject("head");
                                    String code = head.optString("code");
                                    if (code.equals("0")) {//订单入住确认成功
                                        //走二维码支付

                                        BeanWeixin beanWeixin = new BeanWeixin();
                                        beanWeixin.setTotal_fee(1);
                                        beanWeixin.setBody("Body");
                                        beanWeixin.setAttach("setAttach");
                                        beanWeixin.setGoods_tag("1231231");
                                        beanWeixin.setProduct_id("001");
                                        beanWeixin.setDeviceNum("SKW1coykzEgZfoWs");
                                        new PayWeixin().GetPayQrcode(beanWeixin, new IPayCallback() {


                                            @Override
                                            public void onResult(String json) {
                                                LogUtils.d("main::ydong", "生成的二维码链接:" + json);

                                                final String filePath = "/sdcard/reeman/myQr.jpg";
                                                File file = new File(filePath);
                                                if (!file.exists()) {
                                                    file.getParentFile().mkdirs();
                                                    try {
                                                        file.createNewFile();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                JSONObject jsonObject;
                                                String code_url = "";
                                                try {
                                                    jsonObject = new JSONObject(json);
                                                    JSONObject data = jsonObject.optJSONObject("Data");
                                                    code_url = data.optString("code_url");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                boolean qrImage = QRCodeUtil.createQRImage(code_url, 300, 300, BitmapFactory.decodeResource(getResources(), R.drawable.music_round), filePath);
                                                if (qrImage == true) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                Bitmap bit = BitmapFactory.decodeFile(filePath);
                                                                qr_img.setImageBitmap(bit);
                                                            } catch (Exception e) {
                                                                System.out.println("QrCodeActivity : Exception");
                                                            }
                                                        }
                                                    });
                                                }

                                            }

                                            @Override
                                            public void onError(String code, String msg) {
                                                LogUtils.d("main::ydong", "生成的二维码链接失败");
                                            }
                                        });
//                                        onOrderPay();
//                                        PayWeixin.GetPayQrcode();
                                        //支付成功-->进行服务器订单支付接口--> 成功吐卡。


                                        JSONObject jsonObject1 = jsonobj.optJSONObject("biz");
                                        GuestAccount = jsonObject1.optString("orderId");
                                        Log.d("main", "==============订单入住确认---：：：：" + GuestAccount);
                                    } else if (code.equals("1")) {
                                        String err = head.optString("err");
                                        Log.d("main::", "err: " + err);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }

        ).start();

    }

    /**
     * 订单入住取消 SVCE0006
     */
    public void onCancelRoomOrder() {
        JSONObject root = new JSONObject();
        try {

            JSONObject head = new JSONObject();
            head.put("sysCode", "SVCE0006");//应用编码 查询信息，
            head.put("deviceNum", "SKW1coykzEgZfoWs");//设备应用编码 用AI的那个APPID web端看到
            String transactionId = getTransactionId();
            head.put("transactionId", transactionId);//"S10262A010041705161150db9a002657d44b3591d427ae6529f707"
            head.put("sign", getSign(transactionId));
            head.put("reqTime", getStringDate());//改为自己的格式 这个，传参数表示当前的房间情况 当前时间

            root.put("head", head);
            JSONObject biz = new JSONObject();
            biz.put("HotelsID", HotelsID); //酒店ID
            biz.put("GuestAccount", "3081");
            biz.put("CheckInConfirm", "0");//1 确认 0 撤单
            root.put("biz", biz);
        } catch (Exception e) {

        }
        final String json = root.toString();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.d("main", "====================订单入住取消00");
                        HttpUtil.get().postHotel("hotel", HttpUtil.BASE_URL, json, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("main", "==============订单入住取消fail---");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                LogUtils.d("main", "状态码：" + response.code() + "\t【3】订单入住取消body: " + response.body().string());
                            }
                        });
                    }
                }

        ).start();

    }

    /**
     * 订单入住取消 SVCE0006
     */
    public void onCancelRoomOrder(View v) {
        onCancelRoomOrder();
    }

    /**
     * 订单支付 SVCE0005
     *
     * @return
     */
    public void onOrderPay() {
        JSONObject root = new JSONObject();
        try {

            JSONObject head = new JSONObject();
            head.put("sysCode", "SVCE0005");//应用编码 查询信息，
            head.put("deviceNum", "SKW1coykzEgZfoWs");//设备应用编码 用AI的那个APPID web端看到
            String transactionId = getTransactionId();
            head.put("transactionId", transactionId);//"S10262A010041705161150db9a002657d44b3591d427ae6529f707"
            head.put("sign", getSign(transactionId));
            head.put("reqTime", getStringDate());//改为自己的格式 这个，传参数表示当前的房间情况 当前时间

            root.put("head", head);
            JSONObject biz = new JSONObject();
            biz.put("HotelsID", HotelsID);
            biz.put("GuestAccount", GuestAccount);
            biz.put("PayItem", "100");//100 现金  110银联卡
            biz.put("PayAmount", "598");//1块
            biz.put("PayDate", getStringDate());//付款时间
            root.put("biz", biz);

        } catch (Exception e) {

        }
        final String json = root.toString();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        HttpUtil.get().postHotel("hotel", HttpUtil.BASE_URL, json, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("main", "==============fail---");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String json = response.body().string();
                                LogUtils.d("main", response.code() + "=========[4]订单支付: " + json);
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(json);
                                    JSONObject headObj = jsonObject.optJSONObject("head");
                                    String code = headObj.optString("code");
                                    if (code.equals("0")) {
                                        //订单支付成功
                                    } else {
                                        //订单支付失败,取消订单
                                        onCancelRoomOrder();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }

        ).start();
    }

    private final static int phoneNum = 0;//手机号码
    private final static int idCardNum = 1;//身份证号  NO
    private final static int orderId = 2;//订单号
    private final static int userId = 3;//客人ID
    private final static int userName = 4;//姓名  OK

    /**
     * SVCE0007 组合查询订单
     *
     * @param v
     */
    public void onQueryOrder(View v) {
        JSONObject root = new JSONObject();
        try {

            JSONObject head = new JSONObject();
            head.put("sysCode", "SVCE0007");
            head.put("deviceNum", "SKW1coykzEgZfoWs");
            String transactionId = getTransactionId();
            head.put("transactionId", transactionId);
            head.put("sign", getSign(transactionId));
            head.put("reqTime", getStringDate());

            root.put("head", head);
            JSONObject biz = new JSONObject();
            biz.put("HotelsID", HotelsID);
            biz.put("QueryType", userName);
            biz.put("QueryInfo", "yangdong");
            root.put("biz", biz);

        } catch (Exception e) {

        }
        final String json = root.toString();

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        HttpUtil.get().postHotel("hotel", HttpUtil.BASE_URL, json, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("main", "==============fail---");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                LogUtils.d("main", "状态码: " + response.code() + "【5】组合查询订单: " + response.body().string());
                            }
                        });
                    }
                }

        ).start();

    }

    @NonNull
    private String getTransactionId() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYMMDDHHMM");
        String formatdate = simpleDateFormat.format(new Date());
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return "SVCE0001" + "SKW1coykzEgZfoWs" + formatdate + uuid;
    }

    /**
     * @param transactionId 报文流水
     * @return MD5加密后的str
     */
    private String getSign(String transactionId) {
        String deviceKey = "4C5A486D-8E0B-76FB-3AAA-1B571752D133";//平台分配的设备密钥
        return EncryptUtil.MD5(transactionId + deviceKey);
    }


    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH24:mm:ss
     */
    public static String getStringDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date());
    }

}
