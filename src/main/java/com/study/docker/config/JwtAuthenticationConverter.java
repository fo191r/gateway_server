package com.study.docker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

// Esta clase se encarga de convertir los valores de autenticacion del token a algo que spring reconozca para poder hacer
// las validaciones de los roles esta parte me parece que depende del provedor OAUTH2 que estemos usando
@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();

    @Value("${client-id}")
    private String clientId;

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Collection<GrantedAuthority> authorities = Stream
                .concat(converter.convert(source).stream(), extractRolesFromJwt(source).stream())
                .toList();

        return new JwtAuthenticationToken(source, authorities, source.getClaim(JwtClaimNames.SUB));
    }

    private Collection<? extends GrantedAuthority> extractRolesFromJwt(Jwt jwt) {

        // Se obtiene el recurso padre donde estan los roles en el JWT
        if(jwt.getClaim("resource_access") == null){
            return List.of();
        }
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        // Se obtienen la informacion del cliente
        if(resourceAccess.get(clientId) == null){
            return List.of();
        }
        Map<String, Object> clientRoles = (Map<String, Object>) resourceAccess.get(clientId);

        // Se obtiene la informacion de los roles para el cliente
        if(clientRoles.get("roles") == null){
            return List.of();
        }
        Collection<String> roles = (Collection<String>) clientRoles.get("roles");

        // Se convierten los roles para que spring security los pueda usar
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
