package com.lubook.os.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lubook.os.R;

/**
 * Created by wowo on 2017/7/21.
 */

public class VisitorEditDialog extends Dialog {

    public VisitorEditDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void show() {
        super.show();
    }

    public static class Builder {
        private Context mContext;
        private String leftText;
        private String rightText;
        private String title;
        private Drawable iconDrawble;
        private Button ok;
        private Button cancle;
        private ImageView icon_dialog;
        private EditText dialog_edit;
        private View.OnClickListener rightButtonClickListener;
        private View.OnClickListener LeftButtonClickListener;
        private TextView dialog_title;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public String getEditText() {
            String str = dialog_edit.getText().toString().trim();
            Toast.makeText(mContext, "str:" + str, Toast.LENGTH_SHORT).show();
            return str;
        }

        public Builder setLeftText(String leftText) {
            this.leftText = leftText;
            return this;
        }

        public Builder setLeftButtonClickListener(View.OnClickListener LeftButtonClickListener) {
            this.LeftButtonClickListener = LeftButtonClickListener;
            return this;
        }

        public Builder setRightText(String rightText) {
            this.rightText = rightText;
            return this;
        }

        public Builder setIcon(Drawable icon) {
            this.iconDrawble = icon;
            return this;
        }

        public Builder setRightButtonClickListener(View.OnClickListener rightButtonClickListener) {
            this.rightButtonClickListener = rightButtonClickListener;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        public VisitorEditDialog create() {
            final VisitorEditDialog editDialog = new VisitorEditDialog(mContext, R.style.EditDialog);
            View edit_dialog = LayoutInflater.from(mContext).inflate(R.layout.visitor_edit_dialog, null);

            dialog_edit = (EditText) edit_dialog.findViewById(R.id.visitor_company_dialog_edit);
            icon_dialog = (ImageView) edit_dialog.findViewById(R.id.icon_dialog);
            cancle = (Button) edit_dialog.findViewById(R.id.dialog_cancle);
            ok = (Button) edit_dialog.findViewById(R.id.dialog_ok);
            dialog_title = (TextView) edit_dialog.findViewById(R.id.dialog_title);


            if (!TextUtils.isEmpty(leftText)) cancle.setText(leftText);
            if (!TextUtils.isEmpty(rightText)) ok.setText(rightText);
            if (!TextUtils.isEmpty(title)) dialog_title.setText(title);
            if (rightButtonClickListener != null) ok.setOnClickListener(rightButtonClickListener);
            if (LeftButtonClickListener != null) cancle.setOnClickListener(LeftButtonClickListener);
            if (iconDrawble != null) icon_dialog.setBackground(iconDrawble);

            /*设置Dialog显示*/
            Window win = editDialog.getWindow();
            /*宽高*/
            WindowManager.LayoutParams mParams = win.getAttributes();
            mParams.width = 1200;
            mParams.height = 800;/*动态获取屏幕尺寸，如果设置march_parent不起作用*/
            /*位置*/
            mParams.x = 0;
            mParams.y = 0;

            mParams.gravity = Gravity.CENTER | Gravity.RIGHT;

            editDialog.setContentView(edit_dialog, mParams);
            editDialog.setCanceledOnTouchOutside(false);/*外部点击后不退出*/
            return editDialog;

        }

    }


}
