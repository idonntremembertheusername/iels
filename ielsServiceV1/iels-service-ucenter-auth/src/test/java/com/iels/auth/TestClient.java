package com.iels.auth;

import com.iels.framework.client.IelsServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * @Description: 测试Redis的是否连通
 * @Author: snypxk
 * @Date: 2019/12/16 16
 * @Other:
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;

    /*
     * @description: 远程请求spring security 获取令牌
     * @author: snypxk
     * @param
     * @return: void
     *      - 测试是要先启动eureka01 和 服务: XC-SERVICE-UCENTER-AUTH
     **/
    @Test
    public void testClient() {

        //从eureka中获取认证服务的地址(因为spring security在认证服务中)
        //采用客户端负载均衡，从eureka获取认证服务的ip 和 端口
        ServiceInstance serviceInstance = loadBalancerClient.choose(IelsServiceList.IELS_SERVICE_UCENTER_AUTH);
        //serviceInstance.getUri() = http://ip:port
        URI uri = serviceInstance.getUri();
        //完整的令牌申请地址: authUrl [令牌申请地址: POST http://localhost:40400/auth/oauth/token]
        String authUrl = uri + "/auth/oauth/token";

        //请求的内容分两部分: header信息 + body信息
        //1、header信息，包括了http basic认证信息
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
//        String httpbasic = httpbasic("XcWebApp", "XcWebApp");
        String httpbasic = httpbasic("IelsWebApp", "IelsWebApp");
        headers.add("Authorization", httpbasic);
        //2、body信息，包括：grant_type、username、passowrd
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "password");
        body.add("username", "admin");
        body.add("password", "admin");

        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new
                HttpEntity<MultiValueMap<String, String>>(body, headers);

        //如果用户账号不存在 || 用户名对应的密码出错,此时restTemplate调用的服务中spring security会认证出错并抛出400或401异常
        //指定restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应的值为400或401时候也要正常响应，不要抛出异常
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });
        //远程调用申请令牌
        /* RestTemplate.exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType)
              参数解析:
                url: 申请令牌的url
                method: http的方法类型
                requestEntity: 请求内容[requestEntity中可以设置请求头header 和 请求体 body]
                responseType: 响应的结果生成的类型
         */
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
        //exchange.getBody(): 里面包含了申请令牌的令牌
        Map body1 = exchange.getBody();
        System.out.println(body1);
    }

    /*
     * @description: 根据clientId 和 clientSecret 获取其base64编码的 Http Basic串
     * @author: snypxk
     * @param clientId - 客户端ID
     * @param clientSecret - 客户端密码
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
}
