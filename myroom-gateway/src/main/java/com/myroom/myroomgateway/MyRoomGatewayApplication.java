package com.myroom.myroomgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MyRoomGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(MyRoomGatewayApplication.class, args);
		System.out.println("MyRoom Gateway started...");
	}
}
