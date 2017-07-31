package com.Lubook.server.register;


/**
 * Created by wowo on 2017/7/26.
 */

public class RegisterMain {

    public static void main(String[] args) {
        RegisterApi registerApi = new RegisterApi();
        registerApi.register("user", "user", iRegisterCallBack);
    }

    public static IRegisterCallBack iRegisterCallBack = new IRegisterCallBack() {
        @Override
        public void onRegisterSuccess(String code, String msg) {
            System.out.print(code + " \t " + msg);
        }

        @Override
        public void onRegisterError(String code, String msg) {
            System.out.print(code + " \t " + msg);
        }
    };

}
