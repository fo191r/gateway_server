package com.study.docker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilter(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                // Indicamos los paths que deben de tener autenticacion y el rol asociado
//                .authorizeHttpRequests(http -> http
//                        .requestMatchers("/store/**").hasAuthority("admin_client_role")
//                        .requestMatchers("/goods/**").hasAuthority("user_client_role")
//                        .anyRequest().authenticated())

                // Se indica que todos los request deben estar autenticados (lo habilito solo para seguir el ejemplo)
                .authorizeHttpRequests(http -> http.anyRequest().authenticated())

                // Se indica que se configure la conexion con el servidor de recursos para realizar la validacion de tokens
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)

                // Se indica que no se almacenara informacion de las sesiones (usado para servidores de autenticacion y autorizacion)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
