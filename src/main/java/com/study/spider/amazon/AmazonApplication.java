package com.study.spider.amazon;

import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ServletComponentScan
@ComponentScan(basePackages = {"com.study"})
@MapperScan(basePackages = {"com.study.dao"})
//开启定时任务支持
@EnableScheduling
public class AmazonApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmazonApplication.class, args);
    }

}
