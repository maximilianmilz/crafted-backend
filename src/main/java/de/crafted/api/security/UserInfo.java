package de.crafted.api.security;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserInfo {
    String subject;
    String username;
}
