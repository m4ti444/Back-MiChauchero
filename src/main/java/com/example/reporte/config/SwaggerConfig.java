package com.example.reporte.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(
            new Info()
            .title("Reporte PDF API")
            .version("0.1")
            .description("API para generar reportes financieros en PDF")
        );
    }
}
