package com.iels.framework.utils;

import java.util.Random;

/**
 * @Description: 生成手机短信验证码字符串
 * @Author: snypxk
 * @Date: 2019/12/18 15
 * @Other:
 **/
public class VerifyCodeUtils {

    /*
     * @description: 生成指定位数的随机数字
     * @author: snypxk
     * @param len - 随机数的位数
     * @return: java.lang.String
     **/
    public static String generateCode(int len) {
        len = Math.min(len, 8);
        int min = Double.valueOf(Math.pow(10, len - 1)).intValue();
        int num = new Random().nextInt(
                Double.valueOf(Math.pow(10, len + 1)).intValue() - 1) + min;
        return String.valueOf(num).substring(0, len);
    }

    /*
     * @description: 生成6位数的随机数字
     * @author: snypxk
     * @param
     * @return: java.lang.String
     **/
    public static String generateSixCode() {
        int len = Math.min(6, 8);
        int min = Double.valueOf(Math.pow(10, len - 1)).intValue();
        int num = new Random().nextInt(
                Double.valueOf(Math.pow(10, len + 1)).intValue() - 1) + min;
        return String.valueOf(num).substring(0, len);
    }
}
