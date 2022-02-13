package com.miaoubich;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
@ComponentScan({"com.miaoubich.controller", "com.miaoubich.service" })
@EntityScan("com.miaoubich.entity")
@EnableJpaRepositories("com.miaoubich.repository")
public class ResultServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResultServiceApplication.class, args);
	}
	
	@Bean
	public OpenAPI openApiConfig() {
		return new OpenAPI().info(apiInfo());
	}

	private Info apiInfo() {
		Info info = new Info();
		
		info.title("Student Grade API")
			.description("Student grade Api Swagger documentation.")
			.version("v1.0.0")
			.getLicense();
		
		return info;
	}
}
