package com.personal.iphonehouse.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI iphoneHouseApi() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("name",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ))
                .security(List.of(new SecurityRequirement().addList("name")))
                .info(new Info().title("IPHONE HOUSE")
                        .description("Administrate stock")
                        .version("1")
                        .license(new License().name("Apache 2.0").url("nmarinellidev@gmail.com")));

//                .externalDocs(new ExternalDocumentation().description("").url("https://bitbucket.org/AIASAP/pocket-backend-app"));
    }
}
