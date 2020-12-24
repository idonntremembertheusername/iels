package com.iels.govern.gateway.service;

import com.iels.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: 认证服务类
 * @Author: snypxk
 * @Date: 2019/12/17 12
 * @Other:
 **/
@Service
public class AuthService {

    //说明：由于令牌存储时采用 String序列化策略，所以这里用 StringRedisTemplate来查询，使用RedisTemplate无法完成查询
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /*
     * @description: 查询身份令牌
     * @author: snypxk
     * @param request - 请求对象
     * @return: java.lang.String
     **/
    public String getTokenFromCookie(HttpServletRequest request) {
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        String access_token = cookieMap.get("uid");
        if (StringUtils.isEmpty(access_token)) {
            return null;
        }
        return access_token;
    }

    /*
     * @description: 从header中获取jwt令牌
     * @author: snypxk
     * @param request - 请求对象
     * @return: java.lang.String
     **/
    public String getJwtFromHeader(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            //拒绝访问
            return null;
        }
        if (!authorization.startsWith("Bearer ")) {
            //拒绝访问
            return null;
        }
        //取到jwt令牌
        return authorization.substring(7);
    }

    /*
     * @description: 查询令牌的有效期
     * @author: snypxk
     * @param access_token
     * @return: long
     **/
    public long getExpire(String access_token) {
        //token在redis中的key
        String key = "user_token:" + access_token;
        Long expire = stringRedisTemplate.getExpire(key);
        return expire;
    }
}
