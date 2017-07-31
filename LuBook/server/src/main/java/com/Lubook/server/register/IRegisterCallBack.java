package com.Lubook.server.register;

/**
 * Created by wowo on 2017/7/26.
 */

public interface IRegisterCallBack {

    void onRegisterSuccess(String code, String msg);

    void onRegisterError(String code, String msg);

}
