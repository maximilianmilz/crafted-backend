package de.crafted.api.security;

import de.crafted.api.controller.execption.ResourceNotFoundException;
import de.crafted.api.service.user.UserService;
import de.crafted.api.service.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAgent {
    private final UserService userService;

    public Collection<? extends GrantedAuthority> authorities() {
        return getContext().getAuthentication().getAuthorities();
    }

    public boolean hasAuthority(String authorityName) {
        for (GrantedAuthority grantedAuthority : authorities()) {
            if (grantedAuthority.getAuthority().equalsIgnoreCase(authorityName)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRole(String roleName) {
        return hasAuthority("ROLE_" + roleName);
    }

    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken) {
            String subject = getSubFromToken((JwtAuthenticationToken) auth);

            return userService.findBySubject(subject)
                    .orElseThrow(ResourceNotFoundException::new);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private String getSubFromToken(JwtAuthenticationToken token) {
        Jwt principal = (Jwt) token.getPrincipal();
        return principal.getSubject();
    }

}
