package com.example.ainterview.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
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
