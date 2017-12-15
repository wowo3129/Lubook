package com.lubook.os.base;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.tencent.bugly.crashreport.CrashReport;
//import com.mob.MobSDK;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * Created by wowo on 2017/6/23.
 */

public class BaseApplication extends Application {
    private final static String TAG = "main::MeApplication";
    private Context applicationContext;
    public static BaseApplication mInstance;
    public List<BaseActivity> listActivity = null;
    public static final String Bmob_ApplicationId = "36f843363ad767d59df757398f91f1ee";

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        applicationContext = getApplicationContext();
        listActivity = new ArrayList<BaseActivity>();
        initlog();
        initBmob();
//        initMobSDK();
        CrashReport.initCrashReport(getApplicationContext(), "823853352b", true);// bugly 这一句就够了
    }

    private void initBmob() {
        Bmob.initialize(this, Bmob_ApplicationId);
    }

    private void initMobSDK() {
//        MobSDK.init(applicationContext, "1f72500a68afd", "47b2dd0b3d0f6085e20d3eeb91dbb4b1");
    }

    private void initlog() {
        Utils.init(applicationContext);
        LogUtils.Builder builder = new LogUtils.Builder().setBorderSwitch(false).setLog2FileSwitch(true);
    }

    public Context getContext() {
        return applicationContext;
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

    public void AddActivity(BaseActivity a) {
        if (a != null) {
            LogUtils.v(TAG, "addActivity: " + a.getLocalClassName());
            listActivity.add(a);
        }
    }


    public void removeActivity(BaseActivity b) {
        if (b != null) {
            LogUtils.v(TAG, "removeActivity: " + b.getLocalClassName());
            listActivity.remove(b);
            b.finish();
        }
    }

    public void exit() {
        if (listActivity != null && listActivity.size() > 0) {
            for (BaseActivity b : listActivity) {
                listActivity.remove(b);
                b.finish();
            }
        }
    }
}
