package com.example.mvpmongo.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("document-service")
                .pathsToMatch("/document-service/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenApi(
            @Value("${info.app.name}") String appName,
            @Value("${info.app.description}") String appDescription,
            @Value("${info.app.version}") String appVersion) {

        return new OpenAPI()
                .info(new Info().title(appName)
                        .version(appVersion)
                        .description(appDescription));
    }

}
