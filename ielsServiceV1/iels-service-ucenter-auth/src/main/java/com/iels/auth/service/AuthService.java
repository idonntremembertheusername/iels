package com.iels.auth.service;

import com.alibaba.fastjson.JSON;
import com.iels.framework.client.IelsServiceList;
import com.iels.framework.domain.ucenter.ext.AuthToken;
import com.iels.framework.domain.ucenter.response.AuthCode;
import com.iels.framework.exception.ExceptionCast;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/16 22
 * @Other:
 **/
@Slf4j
@Service
public class AuthService {

    @Value("${auth.tokenValiditySeconds}")
    private int tokenValiditySeconds;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /*
     * @description: 认证方法
     * @author: snypxk
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return: com.xuecheng.framework.domain.ucenter.ext.AuthToken
     **/
    public AuthToken login(String username, String password, String clientId, String clientSecret) {
        //申请令牌
        AuthToken authToken = applyToken(username, password, clientId, clientSecret);
        if (authToken == null) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        //将 token存储到redis
        //authToken.setAccess_token((String) map.get("jti"));
        String access_token = authToken.getAccess_token();
        String content = JSON.toJSONString(authToken);
        boolean saveTokenResult = saveToken(access_token, content, tokenValiditySeconds);
        if (!saveTokenResult) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
        }
        return authToken;
    }

    /*
     * @description: 存储令牌到redis
     * @author: snypxk
     * @param access_token  - 短身份令牌内容,即 jti
     * @param content       - authToken对象转成的字符串
     * @param ttl           - 令牌在redis中保存的有效时长,单位:s
     * @return: boolean
     **/
    private boolean saveToken(String access_token, String content, long ttl) {
        // ["user_token:"+access_token] -> key
        String key = "user_token:" + access_token;
        //保存令牌到redis
        stringRedisTemplate.boundValueOps(key).set(content, ttl, TimeUnit.SECONDS);
        //获取过期时间: 如果存入失败返回的时间是一个负数
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire > 0;
    }

    /*
     * @description: 从redis中删除令牌
     * @author: snypxk
     * @param access_token - 短令牌
     * @return: boolean
     **/
    public boolean delToken(String access_token) {
        String key = "user_token:" + access_token;
        stringRedisTemplate.delete(key);
        return true;
    }

    /*
     * @description: 认证方法 - 申请令牌
     * @author: snypxk
     * @param username      - 用户名
     * @param password      - 用户密码
     * @param clientId      - 客户端ID[表 oauth_client_details中的数据: XcWebApp]
     * @param clientSecret  - 客户端ID对应的密码[表 oauth_client_details中的数据: XcWebApp]
     * @return: com.xuecheng.framework.domain.ucenter.ext.AuthToken - 返回AuthToken对象
     **/
    private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {
        //选中认证服务的地址[获取一个服务实例,远程调用这个实例来申请令牌]
        ServiceInstance serviceInstance = loadBalancerClient.choose(IelsServiceList.IELS_SERVICE_UCENTER_AUTH);
        if (serviceInstance == null) {
            log.error("choose an auth instance fail");
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_AUTHSERVER_NOTFOUND);
        }
        //获取令牌的url: http://localhost:40400/auth/oauth/token
        String path = serviceInstance.getUri().toString() + "/auth/oauth/token";
        //定义头
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Authorization", httpbasic(clientId, clientSecret));
        //定义body
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        //授权方式
        formData.add("grant_type", "password");
        //账号
        formData.add("username", username);
        //密码
        formData.add("password", password);

        //指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应的值为400或401时候也要正常响应，不要抛出异常
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });
        //用map来接 申请令牌请求的返回内容体
        Map map = null;
        try {
            //http请求spring security的申请令牌接口
            ResponseEntity<Map> mapResponseEntity = restTemplate.exchange(path, HttpMethod.POST,
                    new HttpEntity<MultiValueMap<String, String>>(formData, header), Map.class);
            map = mapResponseEntity.getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
            log.error("request oauth_token_password error: {}", e.getMessage());
            e.printStackTrace();
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        if (map == null ||
                map.get("access_token") == null ||
                map.get("refresh_token") == null ||
                map.get("jti") == null) {   //jti是jwt令牌的唯一标识作为用户身份令牌
            //解析spring security返回的错误信息
            if (map != null) {
                String error_description = (String) map.get("error_description");
                if (error_description.contains("UserDetailsService returned null")) {
                    ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
                } else if (error_description.contains("坏的凭证")) {
                    ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
                }
            }
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        //申请令牌成功
        AuthToken authToken = new AuthToken();
        //获取访问令牌(jwt)
        String jwt_token = (String) map.get("access_token");
        //获取刷新令牌(jwt)
        String refresh_token = (String) map.get("refresh_token");
        //获取jti: jti作为用户的身份标识
        String access_token = (String) map.get("jti");
        authToken.setJwt_token(jwt_token);
        authToken.setAccess_token(access_token);
        authToken.setRefresh_token(refresh_token);
        return authToken;
    }

    /*
     * @description: 根据clientId 和 clientSecret 获取其base64编码的 Http Basic串
     * @author: snypxk
     * @param clientId      - 客户端ID
     * @param clientSecret  - 客户端密码
     * @return: java.lang.String
     *  http Basic认证:
     *      http协议定义的一种认证方式，将客户端id和客户端密码按照“客户端ID:客户端密码”的格式拼接，
     *      并用base64编码，放在header中请求服务端.
     *      一个例子：Authorization：Basic WGNXZWJBcHA6WGNXZWJBcHA=
     **/
    private String httpbasic(String clientId, String clientSecret) {
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId + ":" + clientSecret;
        //把“客户端id:客户端密码”字符串 进行 base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic " + new String(encode);
    }


    /*
     * @description: 根据身份短令牌从redis查询jwt令牌
     * @author: snypxk
     * @param uid - 份短令牌
     * @return: com.xuecheng.framework.domain.ucenter.ext.AuthToken
     **/
    public AuthToken getUserToken(String uid) {
        String key = "user_token:" + uid;
        String value = stringRedisTemplate.opsForValue().get(key);
        //把字符串转成对象
        try {
            AuthToken authToken = JSON.parseObject(value, AuthToken.class);
            return authToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
