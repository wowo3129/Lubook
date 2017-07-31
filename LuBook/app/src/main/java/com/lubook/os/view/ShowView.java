package com.lubook.os.view;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.lubook.os.R;

/**
 * Created by wowo on 2017/7/31.
 */

public class ShowView extends LinearLayout {

    private final ImageView show_info_img;
    private boolean showInfo = true;
    private int[] showimg = {R.drawable.show_img_info1, R.drawable.show_img_info2, R.drawable.show_img_info3};

    public ShowView(Context context) {
        super(context);
        LinearLayout inflate = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.show_info_view, null);
        show_info_img = (ImageView) inflate.findViewById(R.id.show_info_img);
        show_info_img.setImageDrawable(getResources().getDrawable(showimg[curImg]));
        loopShowInfo();
    }

    int curImg = 0;

    private void loopShowInfo() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (showInfo = true) {
                    LogUtils.d("main::", "loopShowInfo ----11111 showInfo ：" + showInfo);
                    show_info_img.setImageDrawable(getResources().getDrawable(showimg[curImg]));
                    curImg = (curImg + 1) % 3;
                    loopShowInfo();
                } else {
                    LogUtils.d("main::", "loopShowInfo ----22222222：showInfo： " + showInfo);
                }

            }
        }, 1000);
    }

    Handler mHandler = new Handler();

    public void onPause() {
        showInfo = false;
        mHandler.removeCallbacksAndMessages(null);
    }
}
