package com.lubook.os.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lubook.os.R;

/**
 * Created by wowo on 2017/7/21.
 */

public class EditDialog extends Dialog {

    public EditDialog(@NonNull Context context, @StyleRes int themeResId) {
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

        public String getEditText(){
            String str = dialog_edit.getText().toString().trim();
            Toast.makeText(mContext, "str:"+str, Toast.LENGTH_SHORT).show();
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


        public EditDialog create() {
            final EditDialog editDialog = new EditDialog(mContext, R.style.EditDialog);

            View edit_dialog = View.inflate(mContext, R.layout.edit_dialog, null);

            dialog_edit = (EditText) edit_dialog.findViewById(R.id.dialog_edit);
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

//            Window win = editDialog.getWindow();
//            win.getDecorView().setPadding(0, 0, 0, 0);
//            WindowManager.LayoutParams lp = win.getAttributes();
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            win.setAttributes(lp);
//            win.setGravity(Gravity.CENTER);
            editDialog.setContentView(edit_dialog);
            editDialog.setCanceledOnTouchOutside(false);
            return editDialog;

        }

    }


}
