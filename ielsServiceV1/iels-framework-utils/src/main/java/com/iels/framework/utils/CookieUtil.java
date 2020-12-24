package com.iels.framework.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 12
 * @Other:
 **/
public class CookieUtil {

    /*
     * @description: 设置cookie
     * @author: snypxk
     * @param response  - Http响应对象
     * @param domain    - 可以访问该Cookie的域名。如果设置为“.google.com”，则所有以“google.com”结尾的域名都可以访问该Cookie。
     * @param path      - Cookie的使用路径[如果设置为“/”，则本域名下contextPath都可以访问该Cookie。注意最后一个字符必须为“/”。]
     * @param name      - Cookie的名称，Cookie一旦创建，名称便不可更改
     * @param value     - Cookie的值，如果值为Unicode字符，需要为字符编码。如果为二进制数据，则需要使用BASE64编码
     * @param maxAge    - Cookie失效的时间，单位秒。如果为整数，则该Cookie在maxAge秒后失效。
     *                    如果为负数，该Cookie为临时Cookie，关闭浏览器即失效，浏览器也不会以任何形式保存该Cookie。
     *                    如果为0，表示删除该Cookie。默认为-1
     * @param httpOnly  - false 代表浏览器也可以读取该cookie
     * @return: void
     **/
    public static void addCookie(HttpServletResponse response, String domain, String path, String name,
                                 String value, int maxAge, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

    /*
     * @description: 根据cookie名称读取cookie
     * @author: snypxk
     * @param request
     * @param cookieNames
     * @return: java.util.Map<java.lang.String,java.lang.String>
     **/
    public static Map<String, String> readCookie(HttpServletRequest request, String... cookieNames) {
        Map<String, String> cookieMap = new HashMap<String, String>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                String cookieValue = cookie.getValue();
                for (int i = 0; i < cookieNames.length; i++) {
                    if (cookieNames[i].equals(cookieName)) {
                        cookieMap.put(cookieName, cookieValue);
                    }
                }
            }
        }
        return cookieMap;
    }
}
