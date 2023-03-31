package com.kosnik.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.kosnik")
@EntityScan(basePackages = "com.kosnik.domain")
@EnableMongoRepositories(basePackages = "com.kosnik.repository")
public class CompetitorSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompetitorSearchApplication.class, args);
    }
}