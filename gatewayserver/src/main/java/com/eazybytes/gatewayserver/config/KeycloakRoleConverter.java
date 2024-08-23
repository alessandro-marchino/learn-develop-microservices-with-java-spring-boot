package com.eazybytes.gatewayserver.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public Collection<GrantedAuthority> convert(@NonNull Jwt source) {
		Map<String, Object> realmAccess = source.getClaim("realm_access");
		if(realmAccess == null || realmAccess.isEmpty()) {
			return List.of();
		}
		return ((List<String>)realmAccess.get("roles"))
				.stream()
				.map(roleName -> "ROLE_" + roleName)
				.map(SimpleGrantedAuthority::new)
				.map(sga -> (GrantedAuthority)sga)
				.toList();
	}

}
