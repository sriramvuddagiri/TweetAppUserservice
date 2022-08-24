package com.tweetapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApiConfiguration(){
        return new OpenAPI().components(new Components()).info(new Info().title("Tweet App User Service").description("User data manipulation ,authorization .."));

    }
}
