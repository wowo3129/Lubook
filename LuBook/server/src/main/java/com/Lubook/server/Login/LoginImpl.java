package com.Lubook.server.Login;

/**
 * Created by wowo on 2017/7/28.
 */

public class LoginImpl {

    public void login(String name, String password, IloginCallback iloginCallback) {
        if (name.equals(password)) {
            iloginCallback.onLoginSuccess(name + "登陆成功");
        } else {
            iloginCallback.onLoginFailed(name + "登录失败");
        }
    }

    public interface IloginCallback {
        void onLoginSuccess(String msg);
        void onLoginFailed(String msg);
    }
}
