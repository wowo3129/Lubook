package com.Lubook.server.register;

/**
 * Created by wowo on 2017/7/26.
 */

public class RegisterApi {
    public RegisterApi() {
    }

    public void register(String username, String password, IRegisterCallBack callBack) {
        if (username.equals(password)) {
            callBack.onRegisterSuccess("code:0", "注册Success");
        } else {
            callBack.onRegisterError("code:1", "注册Failed");
        }
    }

}
