package org.karane.graphql.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(basic -> {})
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/graphql", "/graphiql/**", "/graphql-ws").permitAll()
                        .anyRequest().permitAll())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
