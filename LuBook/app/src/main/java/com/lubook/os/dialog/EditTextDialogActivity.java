package com.lubook.os.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lubook.os.R;
import com.lubook.os.base.BaseActivity;


public class EditTextDialogActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private EditDialog dialog;
    private EditDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text_dialog);
        mContext = getApplicationContext();
        initView();
    }

    private void initView() {
        Button dialog_EditDialog = (Button) findViewById(R.id.dialog_EditDialog);
        dialog_EditDialog.setOnClickListener(this);
        Button dialog_LoopImgDialog = (Button) findViewById(R.id.dialog_LoopImgDialog);
        dialog_LoopImgDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_EditDialog:
                setNick();
                break;
            case R.id.dialog_LoopImgDialog:
                startloopImgDialog();
                break;
            default:
                break;
        }
    }

    private void startloopImgDialog() {
        LoopImgDialog showDialog = new LoopImgDialog(EditTextDialogActivity.this);/*这里不可以是getApplicationContext()*/
        showDialog.show();
    }

    private void setNick() {
        builder = new EditDialog.Builder(EditTextDialogActivity.this)
                .setIcon(getResources().getDrawable(R.drawable.robot_head))
                .setTitle("自定义机器人昵称")
                .setLeftButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }).setRightButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String editText = builder.getEditText();
                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        this.dialog.show();
    }


}
