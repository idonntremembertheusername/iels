package com.iels.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.iels.framework.model.response.CommonCode;
import com.iels.framework.model.response.ResponseResult;
import com.iels.govern.gateway.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 登录前置过滤器 - 对用户进行身份校验
 * @Author: snypxk
 * @Date: 2019/12/17 12
 * @Other:
 **/
/*
 * 1.从cookie查询用户身份令牌是否存在，不存在则拒绝访问
 * 2.从http header查询jwt令牌是否存在，不存在则拒绝访问
 * 3.从Redis查询user_token令牌是否过期，过期则拒绝访问
 **/
@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    AuthService authService;

    /*
     * @description: 过滤器的类型[四种]
     * @author: snypxk
     * @return: java.lang.String - 返回字符串代表过滤器的类型
     *      FilterConstants.PRE_TYPE    = pre: 请求在被路由之前执行[前置过滤器]
     *      FilterConstants.ROUTE_TYPE  = routing: 在路由请求时调用
     *      FilterConstants.POST_TYPE   = post: 在routing和errror过滤器之后调用
     *      FilterConstants.ERROR_TYPE  = error: 处理请求时发生错误调用
     **/
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /*
     * @description: 过滤器的执行顺序
     * @author: snypxk
     * @return: int - 此方法返回整型数值，通过此数值来定义过滤器的执行顺序，数字越小优先级越高
     **/
    @Override
    public int filterOrder() {
        return 0;
    }

    /*
     * @description: 判断该过滤器是否需要执行
     * @author: snypxk
     * @return: boolean - 返回true表示要执行此过虑器,否则不执行
     **/
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /*
     * @description: 过滤器的业务逻辑
     * @author: snypxk
     * @return: java.lang.Object
     **/
    @Override
    public Object run() throws ZuulException {
        //过虑所有请求，判断头部信息是否有Authorization，如果没有则拒绝访问，否则转发到微服务
        RequestContext currentContext = RequestContext.getCurrentContext();
        //1.获取request
        HttpServletRequest request = currentContext.getRequest();
        //2.获取response
        HttpServletResponse response = currentContext.getResponse();
        //3.从cookie中获取身份令牌
        String tokenFromCookie = authService.getTokenFromCookie(request);
        if (StringUtils.isEmpty(tokenFromCookie)) {
            //如果没有cookie,则拒绝访问
            this.access_denied();
            return null;
        }
        //4.从header中获取jwt
        String jwtFromHeader = authService.getJwtFromHeader(request);
        if (StringUtils.isEmpty(jwtFromHeader)) {
            //如果没有jwt,则拒绝访问
            this.access_denied();
            return null;
        }
        //从redis中取出cookie的过期时间
        long expire = authService.getExpire(tokenFromCookie);
        if (expire < 0) {
            //如果身份令牌过期,则拒绝访问
            this.access_denied();
            return null;
        }
        return null;
    }

    /*
     * @description: 拒绝访问
     * @author: snypxk
     * @return:
     **/
    private void access_denied() {
        //上下文对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        //拒绝访问
        requestContext.setSendZuulResponse(false);
        //设置响应内容
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String responseResultString = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(responseResultString);
        //设置状态码
        requestContext.setResponseStatusCode(200);
        HttpServletResponse response = requestContext.getResponse();
        response.setContentType("application/json;charset=utf-8");
    }
}
