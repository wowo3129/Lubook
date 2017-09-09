package com.lubook.os.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by wowo on 2017/9/5.
 */

public class SharedPreferencesHelper {

    private static final String FILE_NAME = "reeman_user";
    private static Context mContext;
    private static SharedPreferencesHelper instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesHelper(Context mContext) {
        sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferencesHelper init(Context mCt) {
        mContext = mCt;
        instance = new SharedPreferencesHelper(mContext);
        return instance;
    }

    public SharedPreferencesHelper getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesHelper(mContext);
        }
        return instance;
    }

    public void saveData(String key, Object data) {
        String type = data.getClass().getSimpleName();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        editor.commit();
    }


    public Object getData(String key, Object defValue) {

        String type = defValue.getClass().getSimpleName();
        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }
        return null;
    }

    /**
     * 清空数据
     */
    private void clearUserInfo() {
        editor.clear();
        boolean clearFlag = editor.commit();
        LogUtils.d("main", "clearFlag:" + clearFlag);
    }

}
