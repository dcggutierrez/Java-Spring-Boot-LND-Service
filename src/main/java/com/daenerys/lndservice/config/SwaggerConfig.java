package com.daenerys.lndservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String EMPLOYEE_TAG = "Employee Controller";
    public static final String TWC_TAG = "TWC Controller";
    public static final String TRAINING_TAG = "Employee Training Controller";
    public static final String FEEDBACK_TAG = "Feedback Form Controller";
    public static final String USER_TAG = "User Controller";
    public static final String PROFILE_TAG = "Profile Controller";
    public static final String QUICK_LINKS_TAG = "Quick Links Controller";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiDetails())
                .directModelSubstitute(LocalDateTime.class, String.class)
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(LocalTime.class, String.class)
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("com.daenerys"))
                .build()
                .tags(new Tag(EMPLOYEE_TAG, "Operations for employee details"),
                        new Tag(TWC_TAG, "Operations for The Weekday Catch-Up"),
                        new Tag(TRAINING_TAG, "Operations for Employee Training"),
                        new Tag(FEEDBACK_TAG, "Operations for Feedback Forms"),
                        new Tag(USER_TAG, "Operations for user details"),
                        new Tag(PROFILE_TAG, "Operations for user profile"),
                        new Tag(QUICK_LINKS_TAG, "Operations for quick links"));
    }

    private ApiInfo apiDetails() {
        return new ApiInfo(
                "LnD Capstone API",
                "All APIs for LnD Capstone Daenerys Project",
                "1.0",
                "Daenerys",
                new springfox.documentation.service.Contact("Team 3 Backend", "whitecloak.com", "whitecloak.com"),
                "API License",
                "whitecloak.com",
                Collections.emptyList());
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
}
