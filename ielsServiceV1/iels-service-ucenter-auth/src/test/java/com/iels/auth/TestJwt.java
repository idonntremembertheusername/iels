package com.iels.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 本节我们使用Spring Security 提供的JwtHelper来创建JWT令牌，校验JWT令牌等操作
 * @Author: snypxk
 * @Date: 2019/12/16 16
 * @Other:
 **/
/*
 * 1.引入JWT的原因:
 *      传统授权方法的问题是用户每次请求资源服务，资源服务都需要携带令牌访问
 *  认证服务去校验令牌的合法性，并根据令牌获取用户的相关信息，性能低下.
 *
 * 2.使用JWT的思路是:
 *      用户认证通过会得到一个JWT令牌，JWT令牌中已经包括了用户相关的信息，客户端只需要
 * 携带JWT访问资源服务，资源服务根据 事先约定的算法 自行完成 令牌校验，无需每次都请求认证服务完成授权.
 *
 * 3.什么是JWT？
 *      JSON Web Token（JWT）是一个开放的行业标准（RFC 7519），它定义了一种简介的、自包含的协议格式，
 * 用于在通信双方传递json对象，传递的信息经过数字签名可以被验证和信任。JWT可以使用HMAC算法或使用RSA的公
 * 钥/私钥对来签名，防止被篡改。
 *
 * 4.JWT令牌结构:
 *      JWT令牌由三部分组成，每部分中间使用点（.）分隔，比如：xxxxx.yyyyy.zzzzz
 *   xxx: Header: 头部, 包括令牌的类型（即JWT）及使用的哈希算法（如HMAC SHA256或RSA）
 *        [将Header的内容使用Base64Url编码，得到一个字符串就是JWT令牌的第一部分]
 *   yyy: Payload: 负载, 它是存放有效信息的地方，它可以存放jwt提供的现成字段，
 *                 比如：iss（签发者）,exp（过期时间戳）, sub（面向的用户）等，也可自定义字段。
 *                 此部分不建议存放敏感信息，因为此部分可以解码还原原始内容
 *        [分负载使用Base64Url编码，得到一个字符串就是JWT令牌的第二部分]
 *   zzz: Signature: 签名, 此部分用于防止jwt内容被篡改.
 *        [这个部分使用base64url将前两部分进行编码，编码后使用点（.）连接组成字符串，最后使用header中声明签名算法进行签名]
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestJwt {

    /*
     * @description: 生成jwt令牌
     * @author: snypxk
     * @param
     * @return: void
     **/
    @Test
    public void testCreateJwt() {
        //证书文件[文件名+后缀]
        String key_location = "xc.keystore";
        //密钥库密码
        String keystore_password = "xuechengkeystore";
        //密钥的别名
        String alias = "xckey";
        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);
        //创建密钥工厂: 需要证书文件[-keystore：密钥库文件名] 和 密钥库的访问密码[-storepass：密钥库的访问密码]
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource,
                keystore_password.toCharArray());
        //密钥的访问密码，此密码和别名要匹配
        String keypassword = "xuecheng";
        //获取密钥对（密钥和公钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypassword.toCharArray());
        //获取私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        //定义payload信息
        Map<String, Object> tokenMap = new HashMap<>();
        //tokenMap.put("id", "123");
        //tokenMap.put("name", "mrt");
        //tokenMap.put("roles", "r01,r02");
        //tokenMap.put("ext", "1");
        tokenMap.put("name", "itcast");
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(aPrivate));
        //取出jwt令牌
        String token = jwt.getEncoded();
        System.out.println("token=" + token);
    }

    /*
     * @description: 验证jwt令牌
     * @author: snypxk
     * @param
     * @return: void
     *  - 资源服务使用公钥验证jwt的合法性，并对jwt解码
     **/
    @Test
    public void testVerify() {
        //待校验的jwt令牌: testCreateJwt()测试得到的jwt令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1NzY2MDg3ODksImp0aSI6IjA2YWIzOGI3LTczMTEtNDdkOS04MzY0LWQ2MmVmNDE4ZGZlOSIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.LmCxXLBMxhukJpg5-X7YI4XZnPyzcLQKbyS1NwaI6BMBWuWe0Nb-MobjdipMW1tv_k6vkuQwg8kND5CeBmi2sapIegRTBn666p3PvkFqhWgjVdNfp-KZyKStEHUg4UWilsBMcv2ZONdOYMstICnregf5EuRbS8yRFmD7FzBeLk0q889tmUeY475AjbqrN-BsPDkJIkzoTMvU4zKmaEmpHGx-8QjJuysDDAUtI6JMk07LzFAw1cH6obz4QURdl4wjDF41sARFJ9_PfM3PH5NiMjWrnC3xuZH30hr7PnHBgfVMmO8sekhbGoJvUdkoFcRbnpzgagb_qaIxZfAjUH3clQ";
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";
        //校验jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));
        //获取jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
