package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TcpApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(TcpApplication.class, args);
		System.out.println("ok1234==========================");
		Config.Gateway gateway = ctx.getBean(Config.Gateway.class);
		System.out.println("ok1234==========================2");
	}
}
