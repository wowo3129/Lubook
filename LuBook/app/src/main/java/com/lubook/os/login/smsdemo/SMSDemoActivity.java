package com.lubook.os.login.smsdemo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.RegexUtils;
import com.lubook.os.R;

public class SMSDemoActivity extends Activity implements OnClickListener {

    public final static String SMS_SID = "55252684882359b40e8a87151e07a061";/*短信平台 Account sid*/
    public final static String SMS_TOKEN = "9879029bc92488692f3f3e56dad64faf";/*短信平台 Auth Token*/
    public final static String SMS_APPID = "b5ff537edce84f53b1b0feb8c18a84e1";/*短信平台 SMS_APPID*/
    public final static String SMS_TEMPLATEID = "103000";/*短信模板ID SMS_TEMPLATEID*/

    private static final int SMS_SEND_SUCCESS = 0;/*发送短信成功*/
    private static final int SMS_SEND_FAILED = 1;/*发送短信失败*/
    private static final long SEND_SMS_CHECK_CODE_TIME = 60000;/*倒计时总时间60s*/
    private static final long COUNT_DOWN_INTERVAL = 1000; /*倒计时间隔时间1s*/

    private Button btn_enter;/*确认按钮*/
    private Button btn_send_code;/*发送验证码*/
    private EditText edit_code;/*验证码输入框*/
    private EditText edit_phone;/*手机号码输入框*/
    private String check_ode;/*本地生成的验证码*/


    //计时器
    private TimeCount timeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_check_demo);

        btn_enter = (Button) findViewById(R.id.btn_enter);
        btn_send_code = (Button) findViewById(R.id.btn_send_code);
        edit_code = (EditText) findViewById(R.id.edit_code);
        edit_phone = (EditText) findViewById(R.id.edit_phone);

        btn_enter.setOnClickListener(this);
        btn_send_code.setOnClickListener(this);
    }

    /**
     * 内部类计时器
     */
    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            btn_send_code.setText("重新发送验证码");
            btn_send_code.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            btn_send_code.setText((millisUntilFinished / 1000) + "后可重新发送验证码");
            btn_send_code.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        if (btn_enter == v) {//确认按钮
            String code_lin = edit_code.getText().toString();
            if (TextUtils.isEmpty(code_lin)) {
                Toast.makeText(SMSDemoActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (check_ode != null && check_ode.equals(code_lin)) {
                Toast.makeText(SMSDemoActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SMSDemoActivity.this, "验证失败，请填写正确的验证码", Toast.LENGTH_SHORT).show();
            }
        } else if (btn_send_code == v) {//发送验证码
            if (TextUtils.isEmpty(edit_phone.getText().toString())) {
                Toast.makeText(SMSDemoActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isMobileNO(edit_phone.getText().toString())) {
                Toast.makeText(SMSDemoActivity.this, "手机号码不合法", Toast.LENGTH_SHORT).show();
                return;
            }
            sendSMS();
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        return RegexUtils.isMobileExact(mobiles);
    }

    /**
     * 发送短信验证码
     */
    private void sendSMS() {
        check_ode = Math.round(Math.random() * 10000) + "";
        if (check_ode.length() > 4) {
            check_ode = check_ode.substring(0, 4);
        } else if (check_ode.length() < 4) {
            sendSMS();
            return;
        }

        new Thread(new Runnable() {
            public void run() {
                JsonReqClient client = new JsonReqClient();
                String result = client.sendVerificationCode(SMS_APPID, SMS_SID, SMS_TOKEN, check_ode, edit_phone.getText().toString(), SMS_TEMPLATEID);
                //{"resp":{"respCode":"000000","templateSMS":{"createDate":"20140820145658","smsId":"d2c49329f363b802fb3531d9c67b54f8"}}}
                if (result != null && result.length() > 0) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.has("resp")) {
                            JSONObject item = object.getJSONObject("resp");
                            //Code = 105122 同一天同一用户不能发超过10条验证码(因运营商限制一般情况下不足5条)
                            if (item.has("respCode") && item.getString("respCode").equals("000000")) {
                                mHandler.sendEmptyMessage(SMS_SEND_SUCCESS);
                            } else {
                                mHandler.sendEmptyMessage(SMS_SEND_FAILED);
                            }
                        } else {
                            mHandler.sendEmptyMessage(SMS_SEND_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mHandler.sendEmptyMessage(SMS_SEND_FAILED);
                    }
                } else {
                    mHandler.sendEmptyMessage(SMS_SEND_FAILED);
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case SMS_SEND_SUCCESS:
                    Toast.makeText(SMSDemoActivity.this, "短信验证码发送成功", Toast.LENGTH_SHORT).show();

                    timeCount = new TimeCount(SEND_SMS_CHECK_CODE_TIME, COUNT_DOWN_INTERVAL);
                    timeCount.start();

                    break;
                case SMS_SEND_FAILED:
                    Toast.makeText(SMSDemoActivity.this, "短信验证码发送失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
