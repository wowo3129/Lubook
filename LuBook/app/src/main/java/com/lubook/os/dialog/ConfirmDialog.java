package com.lubook.os.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lubook.os.R;


/**
 * Created by wowo on 2017/7/21.
 */

public class ConfirmDialog extends Dialog {

    public ConfirmDialog(@NonNull Context context, @StyleRes int themeResId) {
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
        private View.OnClickListener rightButtonClickListener;
        private View.OnClickListener LeftButtonClickListener;
        private TextView dialog_title;

        public Builder(Context mContext) {
            this.mContext = mContext;
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


        public ConfirmDialog create() {
            final ConfirmDialog editDialog = new ConfirmDialog(mContext, R.style.EditDialog);

            View edit_dialog = View.inflate(mContext, R.layout.confirm_dialog, null);

            icon_dialog = (ImageView) edit_dialog.findViewById(R.id.cunfirm_icon_dialog);
            cancle = (Button) edit_dialog.findViewById(R.id.cunfirm_dialog_cancle);
            ok = (Button) edit_dialog.findViewById(R.id.cunfirm_dialog_ok);
            dialog_title = (TextView) edit_dialog.findViewById(R.id.cunfirm_dialog_title);


            if (!TextUtils.isEmpty(leftText)) cancle.setText(leftText);
            if (!TextUtils.isEmpty(rightText)) ok.setText(rightText);
            if (!TextUtils.isEmpty(title)) dialog_title.setText(title);
            if (rightButtonClickListener != null) ok.setOnClickListener(rightButtonClickListener);
            if (LeftButtonClickListener != null) cancle.setOnClickListener(LeftButtonClickListener);
            if (iconDrawble != null) icon_dialog.setBackground(iconDrawble);


            editDialog.setContentView(edit_dialog);
            editDialog.setCanceledOnTouchOutside(false);
            return editDialog;

        }

    }


}
