package com.study.docker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilter(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                // Indicamos los paths que deben de estar autenticadas y cuales no necesitan autenticacion jwt
                .authorizeHttpRequests(http -> http
                        .requestMatchers("/app/sign").permitAll()
                        .anyRequest().authenticated())

                // Se indica que se configure la conexion con el servidor de recursos para realizar la validacion de tokens, no se añade informacion adicional:
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> {}))

                // Se indica que no se almacenara informacion de las sesiones (usado para servidores de autenticacion y autorizacion)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
