package com.ddng.adminuibootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class AdminUiBootstrapApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(AdminUiBootstrapApplication.class, args);
    }

}