package com.iels.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 测试Redis的是否连通
 * @Author: snypxk
 * @Date: 2019/12/16 16
 * @Other:
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() {
        // 定义key:
        // 请求:　POST http://localhost:40400/auth/oauth/token
        // 相关参数：　<<Authorization>> TYPE=Basic Auth    Username=XcWebApp    Password=XcWebApp
        //            <<body>> application/x-www-form-urlencoded
        //                      grant_type  = password
        //                      username    = itcast
        //                      password    = 123
        // ResponseBody:
        //              "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU3NjU0NjE1OSwianRpIjoiMjc2OWFlMGQtOTYxZS00ZDM3LWI0ZWUtZmZkMWYwN2VhMWMwIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.Kfe14VSoHXxm6sxPKlQ099Yi-J7vi-V06EbR6nqC5PuIVXxqJlCtkj3LhZHMCabvUu8WTMdSgHUd48Jk-7K2vp6CZ-pJ62f3EHTQ5SvbxwEzjNcnteAdQI1NGCUgHpfH2tvNFoky9WGUinzHdnx4zxLP2h0CbwPCmq4-HK1EonEg7sDcPNufdddZsbfF24Hq5OWfV515N2h3BSgY_0slfM82tZleJD1lwQpHyWghRwRAn3dPjBMClEJt08m26FqSpTkgtkseWVONRl2jsTu51Hrpk8zue6ZRSJkZUOe8hT56rAXzBvETJENK8mkJiGssT81nHM8D7AMrKaPtzwWBqQ",
        //              "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJhdGkiOiIyNzY5YWUwZC05NjFlLTRkMzctYjRlZS1mZmQxZjA3ZWExYzAiLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU3NjU0NjE1OSwianRpIjoiNTk1NzdkMjktYzRkNS00YTcyLWIzZWEtNTcwMjEyZDExNjg0IiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.VS_dy4hIdmU_rJW7XkXoVkyWmqEEeyiWtt1qKu8qeA0hJELdEt4DwWTUFl2KYITNEC6JIlzLA4oSFNmZLQ8U7_zj19C3dgzz8sKVsNoMVvu6Bf3BEOmcLPrR_OGuBfEPEEiTktg58i0JXZ1D_Z7ij2L3O6xgI1hLkA8dChaPQK6Wmse_oKlaIm9UfV3aH0LQDo0NJ8vBnK3VsCaqocD589cd9-zFseW2ttNRslIqKVnu0Jf07hpaNQ97eEbLHYbmfESR21l_zWmrKPcOjzHThHQ6jOAlrdfGBENjsNTQ7YqNLmaxPI7o9BYtNIRlKQlZZZxyNE1iXjpzPfOzxYwpPw",
        //              "jti": "2769ae0d-961e-4d37-b4ee-ffd1f07ea1c0"
        String key = "user_token:2769ae0d-961e-4d37-b4ee-ffd1f07ea1c0";
        String access_token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU3NjU0NjE1OSwianRpIjoiMjc2OWFlMGQtOTYxZS00ZDM3LWI0ZWUtZmZkMWYwN2VhMWMwIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.Kfe14VSoHXxm6sxPKlQ099Yi-J7vi-V06EbR6nqC5PuIVXxqJlCtkj3LhZHMCabvUu8WTMdSgHUd48Jk-7K2vp6CZ-pJ62f3EHTQ5SvbxwEzjNcnteAdQI1NGCUgHpfH2tvNFoky9WGUinzHdnx4zxLP2h0CbwPCmq4-HK1EonEg7sDcPNufdddZsbfF24Hq5OWfV515N2h3BSgY_0slfM82tZleJD1lwQpHyWghRwRAn3dPjBMClEJt08m26FqSpTkgtkseWVONRl2jsTu51Hrpk8zue6ZRSJkZUOe8hT56rAXzBvETJENK8mkJiGssT81nHM8D7AMrKaPtzwWBqQ";
        String refresh_token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJhdGkiOiIyNzY5YWUwZC05NjFlLTRkMzctYjRlZS1mZmQxZjA3ZWExYzAiLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU3NjU0NjE1OSwianRpIjoiNTk1NzdkMjktYzRkNS00YTcyLWIzZWEtNTcwMjEyZDExNjg0IiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.VS_dy4hIdmU_rJW7XkXoVkyWmqEEeyiWtt1qKu8qeA0hJELdEt4DwWTUFl2KYITNEC6JIlzLA4oSFNmZLQ8U7_zj19C3dgzz8sKVsNoMVvu6Bf3BEOmcLPrR_OGuBfEPEEiTktg58i0JXZ1D_Z7ij2L3O6xgI1hLkA8dChaPQK6Wmse_oKlaIm9UfV3aH0LQDo0NJ8vBnK3VsCaqocD589cd9-zFseW2ttNRslIqKVnu0Jf07hpaNQ97eEbLHYbmfESR21l_zWmrKPcOjzHThHQ6jOAlrdfGBENjsNTQ7YqNLmaxPI7o9BYtNIRlKQlZZZxyNE1iXjpzPfOzxYwpPw";
        //定义Map
        Map<String, String> mapValue = new HashMap<>();
        mapValue.put("jwt", access_token);
        mapValue.put("refresh_token", refresh_token);
        mapValue.put("username", "itcast");
        //把map转为JSONString
        String jsonString = JSON.toJSONString(mapValue);
        //向redis中存储字符串: 60秒代表的是在Redis中保存的时间
        stringRedisTemplate.boundValueOps(key).set(jsonString, 60, TimeUnit.SECONDS);
        //读取过期时间，已过期返回‐2
        Long expire = stringRedisTemplate.getExpire(key,TimeUnit.SECONDS);
        //根据key获取value
        String s = stringRedisTemplate.opsForValue().get(key);
        System.out.println(s);
    }
}
