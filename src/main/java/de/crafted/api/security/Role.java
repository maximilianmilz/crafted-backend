package de.crafted.api.security;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum Role {
    USER,
    ADMIN;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }

    public String getRole() {
        return this.name();
    }

    public static Role getByAuthority(String authority) {
        return Arrays.stream(Role.values())
                .filter(role -> StringUtils.equalsIgnoreCase(role.name(), "ROLE_" + authority))
                .findFirst()
                .orElse(null);
    }

    public static Role getByRole(String roleValue) {
        return Arrays.stream(Role.values())
                .filter(role -> StringUtils.equalsIgnoreCase(role.name(), roleValue))
                .findFirst()
                .orElse(null);
    }
}