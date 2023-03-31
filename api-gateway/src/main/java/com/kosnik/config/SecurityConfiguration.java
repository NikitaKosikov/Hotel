package com.kosnik.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration{
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity serverHttpSecurity) throws Exception {
        serverHttpSecurity
                .csrf().disable()
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers( "/api/v1/auth/register").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/users/").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/rooms/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/users/").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/users/{id}").hasAnyRole("ADMIN", "USER")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/rooms/{id}").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/payments/").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/payments/").hasAnyRole("ADMIN", "USER")
                        .pathMatchers(HttpMethod.POST, "/api/v1/rooms/").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/v1/orders/").hasRole("ADMIN")
                        .pathMatchers (HttpMethod.POST, "/api/v1/orders/").hasAnyRole("ADMIN", "USER")
                        .pathMatchers (HttpMethod.PUT, "/api/v1/orders/{id}").hasAnyRole("ADMIN", "USER")
                        .anyExchange().authenticated()
                );

        return serverHttpSecurity.build();
    }
}
