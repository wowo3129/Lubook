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
/*

//以下是几种常用调度task的方法：
timer.schedule(task, time);
// time为Date类型：在指定时间执行一次（不周期）。

timer.schedule(task, firstTime, period);
// firstTime为Date类型,period为long
// 从firstTime时刻开始，每隔period毫秒执行一次。

timer.schedule(task, delay) // delay 为long类型：从现在起过delay毫秒之后执行一次（不周期）

timer.schedule(task, delay, period)
// delay为long,period为long：从现在起过delay毫秒以后，每隔period
// 毫秒执行一次。

*/
