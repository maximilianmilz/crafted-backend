package de.crafted.api.security;

import de.crafted.api.service.user.UserService;
import de.crafted.api.service.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
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

    public String getUsername() {
        return getContext().getAuthentication().getName();
    }

    public User getUser() {
        return userService.getUser(getUsername()).orElseThrow();
    }
}