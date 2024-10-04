package com.example.ainterview.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Management API")
                        .version("1.0")
                        .description("API for managing users and their profile images.")
                        .contact(new Contact()
                                .name("Your Name")
                                .url("http://example.com")
                                .email("your-email@example.com")));
    }
}
