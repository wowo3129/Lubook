package com.Lubook.server;

/**
 * Created by wowo on 2017/8/24.
 */

public class timeLoop {
    public static void main(String[] args) {
//        String s = birthdayFormat();
//        System.out.print(s);
    }

    public static String birthdayFormat(String str) {
        String substring1 = str.substring(0, 4);
        String substring2 = str.substring(4, 6);
        String substring3 = str.substring(6, 8);
        return substring1 + "-" + substring2 + "-" + substring3;
    }
}
