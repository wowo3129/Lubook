package com.lubook.os.Bmod;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.lubook.os.R;
import com.lubook.os.base.BaseActivity;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Button user_add = (Button) findViewById(R.id.user_add);
        user_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_add:
//                addUser();
//                upadeUser();
                deleteUserByid();
                break;

            default:
                break;
        }
    }

    private void upadeUser() {
        final User p2 = new User();
        p2.setUsername("北京朝阳");
        p2.update("19c32630ce", new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShort("更新成功:" + p2.getUpdatedAt());
                } else {
                    ToastUtils.showShort("更新成功:" + p2.getUpdatedAt());
                }
            }

        });
    }

    private void deleteUserByid() {
        final User p2 = new User();
        p2.delete("19c32630ce", new UpdateListener() {
            @Override
            public void done(BmobException e) {
                ToastUtils.showShort("" + e.getErrorCode());
            }
        });
    }


    private void addUser() {
        User user = new User();
        user.setUsername("wowo");
        user.setPassword("wowo3129");
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(s);
                } else {
                    ToastUtils.showShort(s);
                }
            }
        });
    }


}
