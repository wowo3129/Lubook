package com.lubook.os.view.windowManager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import com.lubook.os.view.ShowView;

/**
 * 悬浮窗口的管理类(View增加和移除)
 * Created by wowo on 2017/7/31.
 */

public class FloatWindowManager {

    private static WindowManager manager;
    private static ShowView showView;/*图片展览*/

    public static void createShowView(Context mContext) {
        WindowManager windowManger = getWindowManager(mContext);
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManger.getDefaultDisplay().getMetrics(displayMetrics);
        mParams.x = 0;
        mParams.y = 0;
        mParams.width = displayMetrics.widthPixels;
        mParams.height = displayMetrics.heightPixels / 2;
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;/*默人不可点击窗口外部*/
        mParams.format = PixelFormat.RGBA_8888;
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        //自定义View类
        showView = new ShowView(mContext);
        //View对应的窗口位置设置
        windowManger.addView(showView, mParams);
    }

    public static void removeShowView(Context mContext) {
        showView.onPause();
        getWindowManager(mContext).removeView(showView);
    }

    public static void removeAllView(Context mContext) {
        showView.onPause();
        getWindowManager(mContext).removeView(showView);
        //
    }

    public static WindowManager getWindowManager(Context mContext) {
        if (manager == null) {
            manager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }
        return manager;
    }


}
