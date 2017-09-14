package com.glicerial.samples.cardataweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableZuulProxy
@ComponentScan({"com.glicerial.samples.cardataweb", "com.glicerial.samples.cardatacore"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
