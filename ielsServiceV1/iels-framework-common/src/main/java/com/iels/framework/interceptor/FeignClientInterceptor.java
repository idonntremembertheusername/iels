package com.iels.framework.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Description: 微服务之间使用feign进行远程调用，采用feign拦截器实现远程调用携带JWT
 * @Author: snypxk
 * @Date: 2019/12/17 22
 * @Other: 哪个微服务需要通过Feign调用其他微服务, 则在此微服务的启动类中注入[@Bean]该对象即可.
 **/
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            //取出当前请求的Header,里面包含了jwt令牌
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    String headerValue = request.getHeader(headerName);
                    //将header向下传递
                    requestTemplate.header(headerName, headerValue);
                }
            }
        }
    }
}
