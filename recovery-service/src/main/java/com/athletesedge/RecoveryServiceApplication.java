package com.athletesedge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class RecoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecoveryServiceApplication.class, args);
	}

}