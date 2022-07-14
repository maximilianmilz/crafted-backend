package de.crafted.api.controller;

import de.crafted.api.controller.model.UserProfileInput;
import de.crafted.api.security.UserAgent;
import de.crafted.api.service.common.model.Tag;
import de.crafted.api.service.user.UserService;
import de.crafted.api.service.user.model.UserProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/")
public class UserController {

    private final UserService userService;
    private final UserAgent userAgent;

    @Operation(summary = "Get own user profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "User profile not found", content = @Content)
    })
    @GetMapping("/profiles/my")
    public UserProfile getUserProfile() {
        var user = userAgent.getUser();
        return userService.getProfile(user.getId());
    }

    @Operation(summary = "Get user profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "User profile not found", content = @Content)
    })
    @GetMapping("/profiles/{userId}")
    public UserProfile getUserProfile(@PathVariable Long userId) {
        return userService.getProfile(userId);
    }

    @Operation(summary = "Get user profiles.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/profiles")
    public List<UserProfile> getUserProfiles(@RequestParam(required = false) Optional<String> searchTerm,
                                             @RequestParam(required = false) Optional<Boolean> verified,
                                             @RequestParam(required = false) Optional<List<Tag>> tags,
                                             @RequestParam(required = false) Optional<Boolean> bestRatingOrder) {
        return userService.getProfiles(searchTerm, verified, tags, bestRatingOrder);
    }

    @Operation(summary = "Update user profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "User profile not found", content = @Content)
    })
    @PutMapping("/profiles")
    public UserProfile update(@RequestBody UserProfileInput userProfileInput) {
        var user = userAgent.getUser();
        return userService.updateUserProfile(user.getId(), userProfileInput);
    }
}
