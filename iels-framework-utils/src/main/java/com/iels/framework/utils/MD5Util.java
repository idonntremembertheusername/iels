package com.iels.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 12
 * @Other:
 **/
public class MD5Util {
    //The M d5
    static MessageDigest MD5 = null;

    //The Constant HEX_DIGITS
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ne) {
            ne.printStackTrace();
        }
    }

    /*
     * @description: 获取文件md5值
     * @author: snypxk
     * @param file - md5串
     * @return: java.lang.String
     **/
    public static String getFileMD5String(File file) throws IOException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(encodeHex(MD5.digest()));
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException e) {
                throw e;
            }
        }
    }

    /*
     * @description: 获取文件md5值
     * @author: snypxk
     * @param data
     * @return: java.lang.String - md5串
     **/
    public static String getFileMD5String(byte[] data) throws IOException {
        MD5.update(data);
        return new String(encodeHex(MD5.digest()));
    }

    /*
     * @description: Encode hex
     * @author: snypxk
     * @param bytes
     * @return: java.lang.String
     **/
    public static String encodeHex(byte bytes[]) {
        return bytesToHex(bytes, 0, bytes.length);
    }

    /*
     * @description: Bytes to hex
     * @author: snypxk
     * @param bytes
     * @param start
     * @param end
     * @return: java.lang.String
     **/
    public static String bytesToHex(byte bytes[], int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + end; i++) {
            sb.append(byteToHex(bytes[i]));
        }
        return sb.toString();

    }


    /*
     * @description: Byte to hex
     * @author: snypxk
     * @param bt
     * @return: java.lang.String
     **/
    public static String byteToHex(byte bt) {
        return HEX_DIGITS[(bt & 0xf0) >> 4] + "" + HEX_DIGITS[bt & 0xf];
    }

    /*
     * @description: 获取md5值
     * @author: snypxk
     * @param str
     * @return: java.lang.String - md5串
     **/
    public static String getStringMD5(String str) {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] data = str.getBytes("utf-8");
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            MD5.update(data);
            data = MD5.digest();
            for (int i = 0; i < data.length; i++) {
                sb.append(HEX_DIGITS[(data[i] & 0xf0) >> 4] + "" + HEX_DIGITS[data[i] & 0xf]);
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }

//    public static void main(String[] args) {
//        long beginTime = System.currentTimeMillis();
//        File fileZIP = new File("D:\\BaiduNetdiskDownload\\test1.avi");
//        String md5 = "";
//        try {
//            md5 = getFileMD5String(fileZIP);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("MD5:" + md5 + "\n time:" + ((endTime - beginTime)) + "ms");
//        System.out.println(getStringMD5("111111"));
//    }
}
