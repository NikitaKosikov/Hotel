package com.kosnik.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.kosnik.domain")
@EnableMongoRepositories(basePackages = "com.kosnik.repository")
@EntityScan(basePackages = "com.kosnik.entity")
public class RoomApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomApplication.class, args);
    }
}