package com.lubook.os.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lubook.os.R;

/**
 * 轮播显示广告的Dialog
 * Created by wowo on 2017/8/1.
 */

public class LoopImgDialog {

    private final Context mContext;
    private LinearLayout mView;
    private Dialog mDialog;
    private static boolean showInfo = true;
    private int[] showimg = {R.drawable.show_img_info1, R.drawable.show_img_info2, R.drawable.show_img_info3};
    private ImageView show_img;

    public LoopImgDialog(Context context) {
        this.mContext = context;
        init();
        loopShowInfo();
    }

    private void init() {
        mDialog = new Dialog(mContext, R.style.EditDialog);
        mView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.show_info_view, null);/*新布局*/
        ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(500, 500);/*新大小*/
        mDialog.setContentView(mView, mParams);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);/*Dialog透明*/
        show_img = (ImageView) mView.findViewById(R.id.show_info_img);
        show_img.setImageDrawable(mContext.getResources().getDrawable(showimg[curImg]));
        show_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    int curImg = 0;
    Handler mHandler = new Handler();

    private void loopShowInfo() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (showInfo = true) {
                    show_img.setImageDrawable(mContext.getResources().getDrawable(showimg[curImg]));
                    curImg = (curImg + 1) % 3;
                    loopShowInfo();
                }
            }
        }, 1000);
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        showInfo = false;
        mHandler.removeCallbacksAndMessages(null);
        mDialog.dismiss();
    }

}
