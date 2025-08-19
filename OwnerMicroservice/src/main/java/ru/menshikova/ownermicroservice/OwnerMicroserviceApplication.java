package ru.menshikova.ownermicroservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class OwnerMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OwnerMicroserviceApplication.class, args);
	}

}
