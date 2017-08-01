package com.lubook.os.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lubook.os.R;

/**
 * Created by wowo on 2017/8/1.
 */

public class CustomDialog {

    private final Context mContext;
    private LinearLayout mView;
    private Dialog mDialog;

    public CustomDialog(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        mDialog = new Dialog(mContext, R.style.EditDialog);
        mView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.show_info_view, null);/*新布局*/
        ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(500, 500);/*新大小*/
        mDialog.setContentView(mView, mParams);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);/*Dialog透明*/
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }
}
