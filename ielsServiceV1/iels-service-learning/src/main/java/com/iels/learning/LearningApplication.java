package com.iels.learning;

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

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 13
 * @Other:
 **/
@EnableFeignClients     //让该服务可以通过Feign调用其他服务
@EnableDiscoveryClient  //将服务注册到zureka中心
@SpringBootApplication
@EntityScan(value = {"com.iels.framework.domain.learning", "com.iels.framework.domain.task","com.iels.framework.domain.course"})//扫描实体类
@ComponentScan(basePackages = {"com.iels.api"})//扫描接口
@ComponentScan(basePackages = {"com.iels.learning.dao"})//扫描接口
@ComponentScan(basePackages = {"com.iels.framework"})//扫描common下的所有类

public class LearningApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(LearningApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
