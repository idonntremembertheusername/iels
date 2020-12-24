package com.iels.filesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 14
 * @Other:
 **/
@SpringBootApplication//扫描所在包及子包的bean，注入到ioc中
@EntityScan("com.iels.framework.domain.filesystem")//扫描实体类
@ComponentScan(basePackages={"com.iels.api"})//扫描接口
@ComponentScan(basePackages={"com.iels.framework"})//扫描framework中通用类
@ComponentScan(basePackages={"com.iels.filesystem"})//扫描本项目下的所有类
public class FileSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileSystemApplication.class,args);
    }
}
