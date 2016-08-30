package com.example;

import com.example.tcp.Gateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TcpApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(TcpApplication.class, args);
		System.out.println("ok1234==========================");
		Gateway gateway = ctx.getBean(Gateway.class);
		System.out.println("ok1234==========================2");
	}
}
