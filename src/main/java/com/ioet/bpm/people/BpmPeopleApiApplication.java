package com.ioet.bpm.people;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BpmPeopleApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BpmPeopleApiApplication.class, args);
	}
}
