package com.ddng.statisticapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StatisticApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatisticApiApplication.class, args);
	}

}
