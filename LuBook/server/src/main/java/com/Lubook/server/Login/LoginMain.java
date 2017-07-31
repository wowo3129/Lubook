package com.Lubook.server.Login;

/**
 * Created by wowo on 2017/7/28.
 */

public class LoginMain {
    public static void main(String[] args) {
        LoginImpl login = new LoginImpl();
        login.login("wowo", "wowo", new LoginImpl.IloginCallback() {
            @Override
            public void onLoginSuccess(String msg) {
                System.out.print(msg);
            }

            @Override
            public void onLoginFailed(String msg) {
                System.out.print(msg);
            }
        });


    }
}

