package com.lubook.os.base;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wowo on 2017/8/28.
 */
/*
使用手册:
    1 初始化
    private void initCrashHandler() {
        crashHandler = CrashExceptionHandler.getCrashInstance();
        crashHandler.init();
    }
    2 有问题后会触发uncaughtException回调调用writeToSDCard写入SD卡
*/
public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static CrashExceptionHandler carshInstance;

    @Override
    public void uncaughtException(Thread td, Throwable tb) {
        writeToSDCard(td, tb);
        if (td.getId() == 1) {// UI异常
        } else {
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.e("main", "error : ", e);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void init() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void writeToSDCard(Thread td, Throwable tb) {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            File logFile = new File(Environment.getExternalStorageDirectory()
                    .getPath() + "/crashlog");
            if (!logFile.exists()) {
                if (!logFile.mkdirs()) {
                    System.out.println("create crash file fail");
                }
            }
            String logPath = Environment.getExternalStorageDirectory()
                    .getPath()
                    + "/crashlog/"
                    + new SimpleDateFormat("yyMMdd_HHmmss", Locale.getDefault())
                    .format(new Date()) + ".log";
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(logPath);
                PrintStream ps = new PrintStream(fos);
                tb.printStackTrace(ps);
                ps.flush();
                ps.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public CrashExceptionHandler() {
    }

    public static CrashExceptionHandler getCrashInstance() {
        if (carshInstance == null) {
            carshInstance = new CrashExceptionHandler();
        }
        return carshInstance;
    }
}

