package com.Lubook.server;

/**
 * Created by wowo on 2017/8/24.
 */

public class timeLoop {
    public static void main(String[] args) throws InterruptedException {
        long diffTime = (System.currentTimeMillis()) / 1000;
        Thread.sleep(2000);
        long end = (System.currentTimeMillis()) / 1000;
        System.out.print(end - diffTime);
    }
}
