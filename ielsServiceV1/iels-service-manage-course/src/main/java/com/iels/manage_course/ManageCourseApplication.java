package com.iels.manage_course;

import com.iels.framework.interceptor.FeignClientInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients
@EnableDiscoveryClient  //声明为一个EurekaClient从EurekaServer中发现并获取服务
@SpringBootApplication
@EntityScan("com.iels.framework.domain.course")         //扫描实体类
@ComponentScan(basePackages = {"com.iels.api"})         //扫描接口
@ComponentScan(basePackages = {"com.iels.manage_course"})
@ComponentScan(basePackages = {"com.iels.framework"})   //扫描common下的所有类
public class ManageCourseApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ManageCourseApplication.class, args);
    }

    //引入ribbon 和 feign 后需要用RestTemplate进行HTTP调用
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

    // 注入FeignClientInterceptor拦截器,当该服务通过Feign调用其他微服务时,
    // 该拦截器会把头部的令牌令牌向下传递,进而实现微服务间的认证调用
    @Bean
    public FeignClientInterceptor getFeignClientInterceptor() {
        return new FeignClientInterceptor();
    }
}
