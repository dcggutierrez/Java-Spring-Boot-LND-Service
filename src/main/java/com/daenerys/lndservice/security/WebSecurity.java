package com.daenerys.lndservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable().authorizeRequests()
                .mvcMatchers("api/lnd/health-check").permitAll()
                .mvcMatchers(HttpMethod.GET, "api/lnd/employees/**").hasAuthority("read:employee_profile")
                .mvcMatchers("api/lnd/training/request/**").hasAnyAuthority("create:lnd_employee_training", "read:lnd_employee_training")
                .mvcMatchers("api/lnd/twc/request/**").hasAnyAuthority("create:lnd_twc_request", "read:lnd_twc_request")
                .mvcMatchers("/api/lnd/feedback/**").hasAnyAuthority("create:lnd_feedback_form", "read:lnd_feedback_form")
                .mvcMatchers("/api/lnd/requests/own").hasAnyAuthority("read:lnd_twc_request", "read:lnd_employee_training")
                .mvcMatchers("/api/lnd/requests/**").hasAnyAuthority("edit:lnd_twc_request", "edit:lnd_employee_training","read:all_lnd_requests")
                .mvcMatchers("/api/lnd/view/**").hasAnyAuthority("read:all_lnd_feedbacks")
                .mvcMatchers("api/lnd/**").authenticated()
                .and().cors()
                .and().oauth2ResourceServer().jwt();
}

    @Value("${auth0.audience}")
    private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
                JwtDecoders.fromOidcIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter =  new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("permissions");
        converter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtConverter;
    }

}
