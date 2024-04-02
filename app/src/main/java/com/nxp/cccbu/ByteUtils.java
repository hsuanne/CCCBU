package com.nxp.cccbu;

import android.widget.TableRow;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;

public class ByteUtils {
    /*generates an int form 1 byte
    * */
    public static int oneBytesToInt(@NonNull byte b){
        return (int)b&0x000000ff;
    }

    public static int twoBytesToIntBig(@NonNull byte[] b){
        return (0x0000ff00&(b[0]<<8))|(0x000000ff&(b[1]));
    }
    /*generates an int form 4 bytes
     * */
    public static int fourBytesToIntBig(byte[] b){
        return (0x000000ff&(b[3]))|(0x0000ff00&(b[2]<<8))
                |(0x00ff0000&(b[1]<<16))|(0xff000000&(b[0]<<24));
    }

    /*generates an int form 2 bytes
     * */
    public static int twoBytesToInt(@NonNull byte[] b){
        return (0x0000ff00&(b[1]<<8))|(0x000000ff&(b[0]));
    }
    /*generates an int form 4 bytes
     * */
    public static int fourBytesToInt(byte[] b){
        return (0x000000ff&(b[0]))|(0x0000ff00&(b[1]<<8))
                |(0x00ff0000&(b[2]<<16))|(0xff000000&(b[3]<<24));
    }
    /*generates a sub byte array
    src: original byte array
    start:location in @src to start the sub array
    len:sub array's length
    return:a byte array which is a continuous part of @src
     * */
    public static byte[] subByteArray(byte[] src,int start, int len){
        byte[] b = new byte[len];
        System.arraycopy(src,start,b,0,len);
        return b;
    }
    /*
    bytes:byte array to be split
    lens:stores length of each array that after split into from @bytes
    * */
    public static byte[][] splitByteArray(byte[] bytes,int[] lens) throws IOException {
        byte[][] bas = new byte[lens.length][];
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

        for(int i=0;i<lens.length;i++){
            byte[] b= new byte[lens[i]];
            bis.read(b);
            bas[i]=b;
        }
        bis.close();
        return bas;
    }

/*append whole @src to @dest from @pos
* */
    public static void append(byte[] dest,int pos,byte[]src){
        for(byte b :src){
            dest[pos++] = b;
        }
    }
    public static String bytes2String(byte[] data){
        StringBuilder result = new StringBuilder();
        for (byte each : data) {
            result.append(String.format("%02X", each));
        }
        return result.toString();
    }

    public static byte[] int28Bytes(int n){
        byte[] b =  new byte[8];
        b[0] = (byte) (n&0xff);
        b[1] = (byte) (n>>8&0xff);
        b[2] = (byte) (n>>16&0xff);
        b[3] = (byte) (n>>24&0xff);
        b[4]=b[5]=b[6]=b[7]=0;
        return b;
    }

    public static byte[] long2Bytes(long val){
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(val).flip();
        byte[] rst = new byte[Long.BYTES];
        for(int i =0; i<Long.BYTES;i++){
            rst[i]=buffer.get(Long.BYTES-1-i);
        }
        return rst;
    }
    public static byte[] hexStr2Bytes(String hexString) {
        /*
         * int len = paramString.length()/2; byte[] mbytes = new byte[len]; for(int i=0;i<len;i++){ mbytes[i] =
         * (byte)Integer.parseInt(paramString.substring(i*2, i*2+2), 16); } return mbytes;
         */

        if (hexString == null || hexString.isEmpty()) {
            return null;
        }

        hexString = hexString.toUpperCase(Locale.US);

        int length = hexString.length() >> 1;
        char[] hexChars = hexString.toCharArray();

        int i = 0;

//        do {
//            int checkChar = String.valueOf(HEX_ARRAY).indexOf(hexChars[i]);
//
//            if (checkChar == -1)
//                return null;
//            i++;
//        } while (i < hexString.length());

        byte[] dataArr = new byte[length];

        for (i = 0; i < length; i++) {
            int strPos = i * 2;

            dataArr[i] = (byte) (charToByte(hexChars[strPos]) << 4 | charToByte(hexChars[strPos + 1]));
        }

        return dataArr;
    }
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private static byte charToByte(char c) {
        return (byte) String.valueOf(HEX_ARRAY).indexOf(c);
    }

    public static boolean compare(byte[] bs1,byte[]bs2){
        if(bs1.length!=bs2.length){return false;}
        int i =bs1.length;
        while(--i>=0){
            if(bs1[i] != bs2[i]){
                return false;
            }
        }
        return true;
    }
}
