package de.crafted.api.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {
    private final static String OIDC_KEY = "crafted_guest_oidc";
    private final static String BEARER_AUTH_KEY = "bearerAuth";
    private final static List<String> DEFAULT_SCOPES = List.of("openid", "profile", "email");

    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion,
                                 @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {

        var authorizationUrl = issuerUri + "/oauth2/authorize";
        var tokenUrl = issuerUri + "/oauth2/token";

        Scopes scopes = new Scopes();
        DEFAULT_SCOPES.forEach(scope -> {
            scopes.addString(scope, scope);
        });

        OAuthFlows flows = new OAuthFlows();
        OAuthFlow authorizationCodeFlow = new OAuthFlow();
        authorizationCodeFlow.setAuthorizationUrl(authorizationUrl);
        authorizationCodeFlow.setTokenUrl(tokenUrl);
        authorizationCodeFlow.setScopes(scopes);
        flows.authorizationCode(authorizationCodeFlow);

        return new OpenAPI().components(new Components().addSecuritySchemes(OIDC_KEY,
                        new SecurityScheme().type(SecurityScheme.Type.OAUTH2).flows(flows)).addSecuritySchemes(BEARER_AUTH_KEY,
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer")))
                .security(List.of(new SecurityRequirement().addList(OIDC_KEY).addList(BEARER_AUTH_KEY)))
                .info(new Info().title("Crafted App Backend").description("Backend definition for Crafted")
                        .version(appVersion));
    }

}
