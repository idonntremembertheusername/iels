package com.iels.auth;

import com.iels.framework.utils.BCryptUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description: BCryptPasswordEncoder测试
 * @Author: snypxk
 * @Date: 2019/12/16 16
 * @Other:
 **/
/*
 * 早期使用md5对密码进行编码，每次算出的md5值都一样，这样非常不安全，Spring Security推荐
 * 使用BCryptPasswordEncoder对密码加随机盐，每次的Hash值都不一样，安全性高
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestPasswrodEncoder {

    @Test
    public void testPasswrodEncoder() {
        String password = "111111";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (int i = 0; i < 10; i++) {
            //每个计算出的Hash值都不一样
            String hashPass = passwordEncoder.encode(password);
            System.out.println(hashPass);
            //虽然每次计算的密码Hash值不一样但是校验是通过的
            boolean f = passwordEncoder.matches(password, hashPass);
            System.out.println(f);
        }
    }

    @Test
    public void testPasswrodEncoder2() {
        String password = "$2a$10$fFuFF8KMvnx5nSzOX.IMVu2UAYwrmLqhyTquB3SstoY8h2344QlUu";
        boolean xcWebApp = BCryptUtil.matches("IelsWebApp", password);
        boolean student = BCryptUtil.matches("student", password);
        String encodepass = BCryptUtil.encode("student");
        System.out.println(encodepass);
        System.out.println(xcWebApp);
        System.out.println(student);
    }
}
