package com.cdwater.movietimeboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.cdwater")
@MapperScan("com.cdwater.*.mapper")
public class MovieTimeBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieTimeBootApplication.class, args);
    }

}
