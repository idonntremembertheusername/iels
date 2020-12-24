package com.iels.auth.controller;

import com.iels.api.auth.AuthControllerApi;
import com.iels.auth.service.AuthService;
import com.iels.framework.domain.ucenter.ext.AuthToken;
import com.iels.framework.domain.ucenter.request.LoginRequest;
import com.iels.framework.domain.ucenter.response.AuthCode;
import com.iels.framework.domain.ucenter.response.JwtResult;
import com.iels.framework.domain.ucenter.response.LoginResult;
import com.iels.framework.exception.ExceptionCast;
import com.iels.framework.model.response.CommonCode;
import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/16 22
 * @Other: 注意: application.yml中配置了: context-path: /auth,
 * 因此所有请求该服务的前缀都应为: http://localhost:40400/auth
 **/
@RestController
@RequestMapping("/")
public class AuthController implements AuthControllerApi {

    @Value("${auth.clientId}")      //表 oauth_client_details中的数据: IelsWebApp
            String clientId;

    @Value("${auth.clientSecret}")  //表 oauth_client_details中的数据: IelsWebApp
            String clientSecret;

    @Value("${auth.cookieDomain}")  //可以访问该Cookie的域名: .iels.com
            String cookieDomain;

    @Value("${auth.cookieMaxAge}")  //cookie生命时长: 浏览器关闭后cookie就过期
            int cookieMaxAge;

    @Value("${auth.tokenValiditySeconds}")  //token存储到redis的有效时间: 1200s = 20 min
            int tokenValiditySeconds;

    @Autowired
    private AuthService authService;

    /*
     * @description: 登录
     * @author: snypxk
     * @param loginRequest
     * @return: com.iels.framework.domain.ucenter.response.LoginResult
     **/
    @Override
    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest loginRequest) {
        if (loginRequest == null || StringUtils.isEmpty(loginRequest.getUsername())) {
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }
        if (loginRequest == null || StringUtils.isEmpty(loginRequest.getPassword())) {
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        //申请令牌
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);
        //将令牌保存到cookie
        //authToken.getAccess_token()的值其实是令牌的jwt值[即短身份令牌]
        String access_token = authToken.getAccess_token();
        this.saveCookie(access_token);
        return new LoginResult(CommonCode.SUCCESS, access_token);
    }

    /*
     * @description: 退出登录
     * @author: snypxk
     * @param
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Override
    @PostMapping("/userlogout")
    public ResponseResult logout() {
        //取出身份令牌
        String uid = getTokenFormCookie();
        //删除redis中token
        authService.delToken(uid);
        //清除cookie
        this.clearCookie(uid);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /*
     * @description: 查询用户的jwt令牌
     * @author: snypxk
     * @param
     * @return: com.iels.framework.domain.ucenter.response.JwtResult
     **/
    @Override
    @GetMapping("/userjwt")
    public JwtResult userjwt() {
        //1.获取cookie中的令牌[该令牌是身份短令牌]
        String uid = getTokenFormCookie();
        if (StringUtils.isEmpty(uid)) {
            return new JwtResult(CommonCode.FAIL, null);
        }
        //2.根据身份短令牌从redis查询jwt令牌
        AuthToken authToken = authService.getUserToken(uid);
        if (authToken == null) {
            return new JwtResult(CommonCode.FAIL, null);
        }
        //3.将jwt令牌返回给用户
        return new JwtResult(CommonCode.SUCCESS, authToken.getJwt_token());
    }

    /*
     * @description: 从cookie中读取访问令牌[即身份短令牌]
     * @author: snypxk
     * @param
     * @return: java.lang.String
     **/
    private String getTokenFormCookie() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        if (cookieMap.get("uid") != null) {
            return cookieMap.get("uid");
        }
        return null;
    }

    /*
     * @description: 将令牌保存到cookie
     * @author: snypxk
     * @param token - 令牌
     * @return: void
     **/
    private void saveCookie(String token) {
        HttpServletResponse response =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        /*
         * CookieUtil.addCookie(HttpServletResponse response, String domain, String path, String name,
                                 String value, int maxAge, boolean httpOnly)
         * @param domain    - 可以访问该Cookie的域名。如果设置为“.google.com”，则所有以“google.com”结尾的域名都可以访问该Cookie。
         * @param path      - Cookie的使用路径[如果设置为“/”，则本域名下contextPath都可以访问该Cookie]
         * @param name      - Cookie的名称，Cookie一旦创建，名称便不可更改
         * @param value     - Cookie的值，如果值为Unicode字符，需要为字符编码。如果为二进制数据，则需要使用BASE64编码
         * @param maxAge    - Cookie失效的时间，单位秒。如果为整数，则该Cookie在maxAge秒后失效。
         *                    如果为负数，该Cookie为临时Cookie，关闭浏览器即失效，浏览器也不会以任何形式保存该Cookie。
         *                    如果为0，表示删除该Cookie。默认为-1
         * @param httpOnly  - false 代表浏览器也可以读取该cookie
         **/
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, cookieMaxAge, false);
    }

    /*
     * @description: 在cookie中删除token
     * @author: snypxk
     * @param token
     * @return: void
     **/
    private void clearCookie(String token){
        HttpServletResponse response =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, 0, false);
    }
}
