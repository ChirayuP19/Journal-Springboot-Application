package com.ChirayuTech.journalApp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig(){
        return new OpenAPI()
                .info(
                new Info().title("Journal App API'S")
                        .description("By Chirayu")
        )
                .servers(Arrays.asList(new Server().url("http://localhost:8080").description("local"),
                        new Server().url("http://localhost:7070").description("Production")))
                .tags(Arrays.asList(
                        new Tag().name("Public API's"),
                        new Tag().name("User API's"),
                        new Tag().name("Journal API's"),
                        new Tag().name("Admin API's")
                ));
    }
}
