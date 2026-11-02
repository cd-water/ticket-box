package com.cdwater.ticketbox.content;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@MapperScan({
        "com.cdwater.ticketbox.content.movie.adapter.out.persistence.mapper",
        "com.cdwater.ticketbox.content.cinema.adapter.out.persistence.mapper",
        "com.cdwater.ticketbox.content.show.adapter.out.persistence.mapper",
        "com.cdwater.ticketbox.content.play.adapter.out.persistence.mapper",
        "com.cdwater.ticketbox.content.home.adapter.out.persistence.mapper"
})
@SpringBootApplication(scanBasePackages = "com.cdwater.ticketbox")
public class ContentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}
