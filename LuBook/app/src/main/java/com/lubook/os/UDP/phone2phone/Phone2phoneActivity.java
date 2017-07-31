package com.lubook.os.UDP.phone2phone;

import android.os.Bundle;
import com.blankj.utilcode.util.LogUtils;
import com.lubook.os.R;
import com.lubook.os.base.BaseActivity;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Phone2phoneActivity extends BaseActivity {

    private static final String IP_ADDRESS = "192.168.254.36";
    private static final int IP_HOST = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone2phone);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket datagramSocket = new DatagramSocket();
                    byte[] data = "send to Server11111111111你好呀哈".getBytes("utf-8");
                    DatagramPacket datagramPacket = new DatagramPacket(data, data.length, InetAddress.getByName(IP_ADDRESS), IP_HOST);
                    datagramSocket.send(datagramPacket);
                    System.out.print("send data");
                    LogUtils.d("main::","send data");
                    datagramSocket.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

}
