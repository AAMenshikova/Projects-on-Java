package ru.menshikova.webgateway;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class WebGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebGatewayApplication.class, args);
	}

}
