package com.Lubook.server.UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by wowo on 2017/7/24.
 */

public class Server {
    public static void main(String[] args) throws Exception{
        //1、创建DatagramSocket;
        DatagramSocket socket = new DatagramSocket(12345);

        //2、创建数据包，用于接收内容。
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        boolean flag= true;
        while(flag == true){
            //3、接收数据
            socket.receive(packet);
            System.out.println(packet.getAddress().getHostAddress()+":"+packet.getPort());
            //System.out.println(packet.getData().toString());
            //以上语句打印信息错误，因为getData()返回byte[]类型数据，直接toString会将之序列化，而不是提取字符。应该使用以下方法：
            String str = new String(packet.getData(), 0, packet.getData().length,"utf-8");
            System.out.println("receiver: "+str+"2222222222");
            if(str.length()<=0){
                flag= false;
            }
        }
        System.out.print("close server");
        //4、关闭连接。
        socket.close();
    }
}
