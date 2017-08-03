package com.lubook.os.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.lubook.os.R;
import com.lubook.os.utils.UltimateBar;


/**
 * Created by wowo on 2017/6/23.
 */

public class BaseActivity extends Activity {

    public static Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUimatebarInstance(this);
        BaseApplication.getInstance().AddActivity(this);
    }


    UltimateBar ultimateBar = null;

    public UltimateBar getUimatebarInstance(Activity activity) {
        if (ultimateBar == null) {
            ultimateBar = new UltimateBar(activity);
        }
        ultimateBar.setColorBar(ContextCompat.getColor(activity,R.color.colorPrimaryDark),50);
        return ultimateBar;
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
