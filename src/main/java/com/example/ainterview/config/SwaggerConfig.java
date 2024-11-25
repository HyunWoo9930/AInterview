package com.example.ainterview.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		List<Server> serverList = new ArrayList<>();

		Server server = new Server();
		server.setUrl("https://ainterview.hyunwoo9930.store"); // HTTPS 도메인
		server.setDescription("Production Server");
		Server server2 = new Server();
		server2.setUrl("http://localhost:8080"); // HTTPS 도메인
		server2.setDescription("Develop Server");

		serverList.add(server);
		serverList.add(server2);

		return new OpenAPI()
			.addSecurityItem(
				new io.swagger.v3.oas.models.security.SecurityRequirement().addList("Bearer Authentication"))
			.components(new io.swagger.v3.oas.models.Components()
				.addSecuritySchemes("Bearer Authentication", new io.swagger.v3.oas.models.security.SecurityScheme()
					.type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")));
	}
}
