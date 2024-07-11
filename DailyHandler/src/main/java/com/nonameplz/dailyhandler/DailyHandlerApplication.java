package com.nonameplz.dailyhandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DailyHandlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyHandlerApplication.class, args);
    }

}
