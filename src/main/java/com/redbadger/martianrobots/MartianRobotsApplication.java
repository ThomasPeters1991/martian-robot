package com.redbadger.martianrobots;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Martian Robots API", version = "1.0", description = "API for simulating the movement of robots on Mars"))
public class MartianRobotsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MartianRobotsApplication.class, args);
	}

}
