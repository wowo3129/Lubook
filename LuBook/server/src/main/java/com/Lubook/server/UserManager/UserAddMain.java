package com.Lubook.server.UserManager;

/**
 * Created by wowo on 2017/8/4.
 */

public class UserAddMain {
    public static void main(String[] args) {

        UserControl userControl = new UserControl();
        userControl.addUser("yangdong", "yangdong", new UserControl.IUserCallback() {

            @Override
            public void onSuccess(String code, String name) {

            }

            @Override
            public void onFail(String code, String name) {

            }
        });
    }
}
