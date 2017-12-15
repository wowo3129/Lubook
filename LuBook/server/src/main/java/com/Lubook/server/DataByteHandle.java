package com.Lubook.server;

import sun.rmi.runtime.Log;

/**
 * Created by wowo on 2017/10/17.
 */

public class DataByteHandle {
    public static void main(String[] args) {
        byte xor_data = (byte)0x00;
        byte[] data = new byte[100];
        data[0] = (byte) 0x06;
        data[1] = (byte) 0xf2;
        data[2] = (byte) 0x00;
        data[3] = (byte) 0x00;
        data[4] = (byte) 0x06;
        data[5] = (byte) 0x50;
        data[6] = (byte) 0x32;
        data[7] = (byte) 0x30;
        data[8] = (byte) 0x31;
        data[9] = (byte) 0x31;
        data[10] = (byte) 0x30;
        data[11] = (byte) 0x03;
        data[12] = (byte) 0x95;//校验位
        xor_data = data[1];
        for (int i = 2; i < data.length - 1; i++) {
            xor_data = (byte) (data[i] ^ xor_data);
        }
        if (data[1] == data[13 - 1]) {//13 -1 = 12
            System.out.print("success --------wowo" + 13);
        }


        for (int i = 0; i < data.length; i++) {
            if(data[i] <1 ){
                System.out.print("0x" + Integer.toHexString(0xff & data[i]) + "\t");
            }
        }


    }
}

