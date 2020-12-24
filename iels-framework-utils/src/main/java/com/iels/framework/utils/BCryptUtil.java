package com.iels.framework.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 12
 * @Other:
 **/
public class BCryptUtil {

    /*
     * @description: 把密码进行加密处理
     * @author: snypxk
     * @param password - 待加密的密码
     * @return: java.lang.String - 加密后的密码
     **/
    public static String encode(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /*
     * @description: 比较某一个密码是否匹配 其加密后的密码
     * @author: snypxk
     * @param password - 密码
     * @param hashPass - 加密后的密码
     * @return: boolean - 如果匹配返回true,不匹配返回false
     **/
    public static boolean matches(String password, String hashPass) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, hashPass);
    }
}
