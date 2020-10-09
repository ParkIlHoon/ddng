package com.ddng.adminuibootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AdminUiBootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminUiBootstrapApplication.class, args);
    }

}
