package com.lubook.os.login.SMS_mod;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.lubook.os.R;
import com.lubook.os.base.BaseActivity;
import com.lubook.os.groupphoto.ImageUploadActivity;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

public class SmsLoginActivity extends BaseActivity implements View.OnClickListener {

    private Button login_btn;
    private Button et_sms_code_btn;
    private EditText et_sms_code_value;
    private RelativeLayout sms_relativeView;
    private EditText et_phonenum;
    private EventHandler eventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_login);

        initView();
        initSMS();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    private void initView() {
        et_sms_code_value = (EditText) findViewById(R.id.et_sms_code_value);
        sms_relativeView = (RelativeLayout) findViewById(R.id.sms_relativeView);
        et_sms_code_btn = (Button) findViewById(R.id.et_sms_code_btn);

        et_phonenum = (EditText) findViewById(R.id.et_phonenum);

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
        et_sms_code_btn.setOnClickListener(this);
        et_sms_code_btn.setVisibility(View.INVISIBLE);
        et_sms_code_value.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        String phone_num = et_phonenum.getText().toString().trim();
        String sms_code = et_sms_code_value.getText().toString().trim();
        switch (v.getId()) {
            case R.id.login_btn:/*登陆*/
                if(!TextUtils.isEmpty(phone_num) && RegexUtils.isMobileExact(phone_num)){
                    SMSSDK.submitVerificationCode("86", phone_num, sms_code);/*String country, String phone, String code*/
                }else{
                    SnackbarUtils.showShort(sms_relativeView,"请您输入正确的手机号!", Color.WHITE,Color.BLUE);
                }

                break;
            case R.id.et_sms_code_btn:/*获取验证码*/
                SMSSDK.getSupportedCountries();
                SMSSDK.getVerificationCode("86", phone_num, new OnSendMessageHandler() {
                    @Override
                    public boolean onSendMessage(String country, String phone) {
                        return false;
                    }
                });
                break;
            default:
                break;
        }
    }

    private void initSMS() {
        // 创建EventHandler对象
        // 处理你自己的逻辑
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (data instanceof Throwable) {
                    Throwable throwable = (Throwable) data;
                    String msg = throwable.getMessage();
                    LogUtils.d("main::", "msg:\n" + msg);
                } else {
                    if (event == SMSSDK.RESULT_COMPLETE) {
                        boolean smart = (boolean) data;
                        if (smart == true) {/*智能短信验证*/
                            SnackbarUtils.showShort(sms_relativeView,"智能短信验证", Color.WHITE,Color.BLUE);
                            startActivity(new Intent(SmsLoginActivity.this,ImageUploadActivity.class));
                        } else {
                            et_sms_code_btn.setVisibility(View.VISIBLE);
                            et_sms_code_value.setVisibility(View.VISIBLE);
                            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                SnackbarUtils.showShort(sms_relativeView,"验证码已发送，等待手机端接收", Color.WHITE,Color.BLUE);
                            } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                                Log.d("main::", "返回支持发送验证码的国家列表:\n" + data.toString());
                            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                SnackbarUtils.showShort(sms_relativeView,"验证码验证成功，进去主界面", Color.WHITE,Color.BLUE);
                                startActivity(new Intent(SmsLoginActivity.this,ImageUploadActivity.class));
                            }
                        }
                    }
                }
            }

            @Override
            public void onRegister() {
                super.onRegister();
            }

            @Override
            public void beforeEvent(int i, Object o) {
                super.beforeEvent(i, o);
            }

            @Override
            public void onUnregister() {
                super.onUnregister();
            }
        };

        // 注册监听器
        SMSSDK.registerEventHandler(eventHandler);
    }

}
