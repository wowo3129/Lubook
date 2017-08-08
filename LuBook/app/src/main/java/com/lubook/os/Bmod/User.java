package com.lubook.os.Bmod;

import cn.bmob.v3.BmobObject;

/**
 * Created by wowo on 2017/8/8.
 */

public class User extends BmobObject {
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
