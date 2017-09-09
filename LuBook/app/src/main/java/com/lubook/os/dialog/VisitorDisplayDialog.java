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

public class VisitorDisplayDialog extends Dialog {

    public VisitorDisplayDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void show() {
        super.show();
    }

    public static class Builder {
        private Context mContext;
        private String title;
        private Drawable iconDrawble;
        private Button visitor_close_btn;
        private ImageView icon_dialog;
        private View.OnClickListener rightButtonClickListener;
        private TextView dialog_title;

        public Builder(Context mContext) {
            this.mContext = mContext;
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


        public VisitorDisplayDialog create() {
            final VisitorDisplayDialog editDialog = new VisitorDisplayDialog(mContext, R.style.EditDialog);

            View edit_dialog = View.inflate(mContext, R.layout.visitor_display_dialog, null);

            icon_dialog = (ImageView) edit_dialog.findViewById(R.id.cunfirm_icon_dialog);
            visitor_close_btn = (Button) edit_dialog.findViewById(R.id.visitor_close_btn);
            dialog_title = (TextView) edit_dialog.findViewById(R.id.cunfirm_dialog_title);


            if (!TextUtils.isEmpty(title)) dialog_title.setText(title);
            if (rightButtonClickListener != null) visitor_close_btn.setOnClickListener(rightButtonClickListener);
            if (iconDrawble != null) icon_dialog.setBackground(iconDrawble);

            editDialog.setContentView(edit_dialog);
            editDialog.setCanceledOnTouchOutside(false);
            return editDialog;

        }

    }


}
