package com.athletesedge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PlannerServiceApplication {

	/**
	 * @param args command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(PlannerServiceApplication.class, args);
	}

}