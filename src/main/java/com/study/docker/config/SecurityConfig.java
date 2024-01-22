package com.study.docker.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private JwtAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    SecurityFilterChain securityFilter(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                // Indicamos los paths que deben de tener autenticacion y el rol asociado
                .authorizeHttpRequests(http -> http
                        .requestMatchers("/app/sign").permitAll()
                        .requestMatchers("/store/**").hasRole("admin_client")
                        .requestMatchers("/goods/**").hasRole("user_client")
                        .anyRequest().authenticated())

                // Se indica que se configure la conexion con el servidor de recursos para realizar la validacion de tokens
                // Se indica un conversor para obtener los roles del JWT y asignarlos dentro del contexto de spring security
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))

                // Se indica que no se almacenara informacion de las sesiones (usado para servidores de autenticacion y autorizacion)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
