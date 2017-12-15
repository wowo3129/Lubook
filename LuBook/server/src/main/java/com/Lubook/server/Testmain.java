package com.Lubook.server;


/**
 * Created by wowo on 2017/11/17.
 */

public class Testmain {

    public static void main(String[] args) {
        String data = "3 0106 0001 201711301423201712101200  210324198006231611 1 1";
        byte[] encode = Encode(data);
        for (int i = 0; i < encode.length; i++) {
            System.out.print(Integer.toHexString(0xff & encode[i]) + "\t");
        }
    }

    /**
     * String data = "3 0106 0001 201711301423201712101200  210324198006231611 1 1";
     *
     * @param data 发送指令
     * @return 经过处理的的ASSII编码 aa    55	3c	33	20	30	31	30	36	20	30	30	30	31	20	32	30	31	37	31	31	33	30	31	34	32	33	32	30	31	37	31	32	31	30	31	32	30	30	20	20	32	31	30	33	32	34	31	39	38	30	30	36	32	33	31	36	31	31	20	31	20	31	29
     */
    static byte[] Encode(String data) {

        int i = 0;
        byte xor = 0x00;
        byte[] encode_data = new byte[data.length() + 4];
        encode_data[0] = (byte) 0xaa;
        encode_data[1] = 0x55;
        xor = (byte) data.length();
        encode_data[2] = xor;
        for (i = 0; i < data.length(); i++) {
            xor ^= (byte) data.charAt(i);
            encode_data[i + 3] = (byte) data.charAt(i);
        }
        encode_data[i + 3] = xor;
        return encode_data;
    }
}
