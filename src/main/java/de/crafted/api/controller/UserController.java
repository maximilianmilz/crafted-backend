package de.crafted.api.controller;

import de.crafted.api.security.UserAgent;
import de.crafted.api.service.user.UserService;
import de.crafted.api.service.user.model.UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/")
public class UserController {

    private final UserService userService;
    private final UserAgent userAgent;

    @GetMapping("/profile/")
    public UserProfile getUserProfile() {
        var user = userAgent.getUser();
        return userService.getProfile(user.getId());
    }

    @GetMapping("/{userId}/profile")
    public UserProfile getUserProfile(@PathVariable Long userId) {
        return userService.getProfile(userId);
    }

    @GetMapping("/")
    public List<UserProfile> getUserProfiles() {
        return userService.getProfiles();
    }
}
