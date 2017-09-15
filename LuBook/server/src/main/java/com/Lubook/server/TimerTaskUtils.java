package com.Lubook.server;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wowo on 2017/8/23.
 */

public class TimerTaskUtils {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.print("123");
            }
        }, 200, 1000);/*task,firsttime,period 每间隔1s重复一次*/
    }
}

