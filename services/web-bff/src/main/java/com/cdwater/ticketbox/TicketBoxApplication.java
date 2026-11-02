package com.cdwater.ticketbox;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDubbo
@EnableScheduling
@SpringBootApplication
public class TicketBoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketBoxApplication.class, args);
    }

}
