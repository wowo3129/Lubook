package com.lubook.os.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 * Created by wowo on 2017/6/23.
 */

public class BaseActivity extends Activity {

    public static Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getInstance().AddActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getInstance().removeActivity(this);
    }

    /**
     * 退出应用
     */
    public void exit() {
        BaseApplication.getInstance().exit();
    }


}
