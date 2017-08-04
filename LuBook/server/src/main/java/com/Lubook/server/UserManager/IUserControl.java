package com.Lubook.server.UserManager;

/**
 * Created by wowo on 2017/8/4.
 */

public interface IUserControl {

    public void addUser(String password, String name, UserControl.IUserCallback callback);

    public void deleteUser(String password, String name, UserControl.IUserCallback callback);

    public void updateUser(String password, String name, UserControl.IUserCallback callback);

    public void searchUser(String password, String name, UserControl.IUserCallback callback);
}

