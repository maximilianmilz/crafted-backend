package de.crafted.api.security;


import de.crafted.api.service.user.UserService;
import de.crafted.api.service.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken) {
            var userInfo = getUserInfo((JwtAuthenticationToken) auth);
            Optional<User> user = userService.getUser(userInfo.getSubject());

            if (user.isEmpty()) {
                createUser(userInfo);
            }

        }
        return true;
    }

    private User createUser(User userInfo) {
        User user = userService.createUser(userInfo.getSubject(), userInfo.getUsername());
        log.debug("Created new user. {}", user);
        return user;
    }

    private User getUserInfo(JwtAuthenticationToken token) {
        Jwt principal = (Jwt) token.getPrincipal();
        return User.builder()
                .subject(principal.getSubject())
                .username(principal.getClaim("username"))
                .build();
    }
}