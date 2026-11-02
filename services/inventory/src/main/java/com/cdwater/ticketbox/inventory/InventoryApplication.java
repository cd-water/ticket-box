package com.cdwater.ticketbox.inventory;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDubbo
@EnableScheduling
@MapperScan("com.cdwater.ticketbox.inventory.adapter.out.persistence.mapper")
@SpringBootApplication(scanBasePackages = "com.cdwater.ticketbox")
public class InventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }
}
