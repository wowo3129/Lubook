package com.Lubook.server.UserManager;

/**
 * Created by wowo on 2017/8/4.
 */

public class UserControl implements IUserControl {
    @Override
    public void addUser(String password, String name, IUserCallback callback) {
        callback.onSuccess(password, name);
        callback.onFail(password, name);
    }

    @Override
    public void deleteUser(String password, String name, IUserCallback callback) {

    }

    @Override
    public void updateUser(String password, String name, IUserCallback callback) {

    }

    @Override
    public void searchUser(String password, String name, IUserCallback callback) {

    }

    public interface IUserCallback {
        void onSuccess(String code, String name);

        void onFail(String code, String name);
    }
}

