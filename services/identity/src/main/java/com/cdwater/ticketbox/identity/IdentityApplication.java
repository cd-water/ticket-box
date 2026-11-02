package com.cdwater.ticketbox.identity;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@MapperScan({
        "com.cdwater.ticketbox.user.adapter.out.persistence.mapper",
        "com.cdwater.ticketbox.admin.adapter.out.persistence.mapper"
})
@SpringBootApplication(scanBasePackages = "com.cdwater.ticketbox")
public class IdentityApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityApplication.class, args);
    }
}
