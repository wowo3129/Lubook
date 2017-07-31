package com.lubook.os;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lubook.os.base.BaseActivity;
import com.lubook.os.view.windowManager.FloatWindowManager;

public class Launcher extends BaseActivity implements View.OnClickListener {

    private Button main_show_info_img, main_exit_show_info_img;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mContext = getApplicationContext();
        initView();

    }

    private void initView() {
        main_show_info_img = (Button) findViewById(R.id.main_show_info_img);
        main_exit_show_info_img = (Button) findViewById(R.id.main_exit_show_info_img);
        main_show_info_img.setOnClickListener(this);
        main_exit_show_info_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_show_info_img:
                FloatWindowManager.createShowView(mContext);
                break;
            case R.id.main_exit_show_info_img:
                FloatWindowManager.removeShowView(mContext);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
