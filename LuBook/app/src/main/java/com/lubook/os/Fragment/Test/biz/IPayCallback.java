package com.lubook.os.Fragment.Test.biz;

/**
 * Created by ZJcan on 2017-08-24.
 */

public interface IPayCallback {
    void onResult(String json);

    void onError(String code, String msg);
}
