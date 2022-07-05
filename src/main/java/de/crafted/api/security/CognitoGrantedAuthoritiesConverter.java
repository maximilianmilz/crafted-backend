package de.crafted.api.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static de.crafted.api.security.Role.USER;

public class CognitoGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(final Jwt jwt) {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(USER.getAuthority()));
        List<String> cognitoGroups = jwt.getClaimAsStringList("cognito:groups");
        if (cognitoGroups != null) {
            cognitoGroups.stream()
                    .map(Role::getByRole)
                    .filter(Objects::nonNull)
                    .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                    .forEach(authorities::add);
        }

        return authorities;
    }
}
