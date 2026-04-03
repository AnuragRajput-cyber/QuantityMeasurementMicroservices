package com.app.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI authServiceOpenApi() {
        return new OpenAPI().info(new Info()
                .title("QMA Auth Service API")
                .version("1.0")
                .description("Authentication endpoints for register, login, and current-user access."));
    }
}
