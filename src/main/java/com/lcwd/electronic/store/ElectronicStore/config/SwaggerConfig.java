package com.lcwd.electronic.store.ElectronicStore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket docket()
    {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(getApiInfo());
        docket.securityContexts(Arrays.asList(getScurityContext()));
        docket.securitySchemes(Arrays.asList(getSchemes()));

        ApiSelectorBuilder select = docket.select();
        select.apis(RequestHandlerSelectors.any());
        select.paths(PathSelectors.any());
        Docket build = select.build();

        return docket;

    }

    private SecurityContext getScurityContext(){

        SecurityContext context = SecurityContext
                .builder()
                .securityReferences(getSecurityReferences())
                .build();
        return context;
    }

    private List<SecurityReference> getSecurityReferences() {
        AuthorizationScope [] scopes = {
                new AuthorizationScope("Global","Access Every thing")
        };
        return Arrays.asList(new SecurityReference("apikey",scopes));
    }

    private ApiKey getSchemes(){

        return new ApiKey("apikey","Authorization","header");
    }

    private ApiInfo getApiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Electronic Store BackEnd :APIS",
                "This backend project created by Ajay",
                "1.0.0V",
                "https://www.backendByAjay.com",
                new Contact("Ajay","https//www.instagram.com/durgesh_k_t","test@gmail.com"),
                "License of APIS",
                "https://www.backendByAjay.com",
                new ArrayDeque<>()



        );
        return apiInfo;

    }

}
