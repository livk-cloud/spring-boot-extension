package com.livk.auth.server.autoconfigure;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author livk
 */
@Data
@ConfigurationProperties(OAuth2AuthorizationServerProperties.PREFIX)
public class OAuth2AuthorizationServerProperties implements InitializingBean {

    public static final String PREFIX = "spring.security.oauth2.authorizationserver";
    /**
     * Registered clients of the Authorization Server.
     */
    private final Map<String, Client> client = new HashMap<>();
    /**
     * Authorization Server endpoints.
     */
    private final Endpoint endpoint = new Endpoint();
    /**
     * URL of the Authorization Server's Issuer Identifier.
     */
    private String issuer;

    @Override
    public void afterPropertiesSet() {
        validate();
    }

    public void validate() {
        getClient().values().forEach(this::validateClient);
    }

    private void validateClient(Client client) {
        if (!StringUtils.hasText(client.getRegistration().getClientId())) {
            throw new IllegalStateException("Client id must not be empty.");
        }
        if (CollectionUtils.isEmpty(client.getRegistration().getClientAuthenticationMethods())) {
            throw new IllegalStateException("Client authentication methods must not be empty.");
        }
        if (CollectionUtils.isEmpty(client.getRegistration().getAuthorizationGrantTypes())) {
            throw new IllegalStateException("Authorization grant types must not be empty.");
        }
    }

    /**
     * Authorization Server endpoints.
     */
    @Data
    public static class Endpoint {

        /**
         * OpenID Connect 1.0 endpoints.
         */
        @NestedConfigurationProperty
        private final OidcEndpoint oidc = new OidcEndpoint();
        /**
         * Authorization Server's OAuth 2.0 Authorization Endpoint.
         */
        private String authorizationUri;
        /**
         * Authorization Server's OAuth 2.0 Token Endpoint.
         */
        private String tokenUri;
        /**
         * Authorization Server's JWK Set Endpoint.
         */
        private String jwkSetUri;
        /**
         * Authorization Server's OAuth 2.0 Token Revocation Endpoint.
         */
        private String tokenRevocationUri;
        /**
         * Authorization Server's OAuth 2.0 Token Introspection Endpoint.
         */
        private String tokenIntrospectionUri;

    }

    /**
     * OpenID Connect 1.0 endpoints.
     */
    @Data
    public static class OidcEndpoint {

        /**
         * Authorization Server's OpenID Connect 1.0 Client Registration Endpoint.
         */
        private String clientRegistrationUri;

        /**
         * Authorization Server's OpenID Connect 1.0 UserInfo Endpoint.
         */
        private String userInfoUri;
    }

    /**
     * A registered client of the Authorization Server.
     */
    @Data
    public static class Client {

        /**
         * Client registration information.
         */
        @NestedConfigurationProperty
        private final Registration registration = new Registration();
        /**
         * Token settings of the registered client.
         */
        @NestedConfigurationProperty
        private final Token token = new Token();
        /**
         * Whether the client is required to provide a proof key challenge and verifier
         * when performing the Authorization Code Grant flow.
         */
        private boolean requireProofKey;
        /**
         * Whether authorization consent is required when the client requests access.
         */
        private boolean requireAuthorizationConsent;
        /**
         * URL for the client's JSON Web Key Set.
         */
        private String jwkSetUri;
        /**
         * JWS algorithm that must be used for signing the JWT used to authenticate the
         * client at the Token Endpoint for the {@code private_key_jwt} and
         * {@code client_secret_jwt} authentication methods.
         */
        private String tokenEndpointAuthenticationSigningAlgorithm;
    }

    /**
     * Client registration information.
     */
    @Data
    public static class Registration {

        /**
         * Client ID of the registration.
         */
        private String clientId;

        /**
         * Client secret of the registration. May be left blank for a public client.
         */
        private String clientSecret;

        /**
         * Name of the client.
         */
        private String clientName;

        /**
         * Client authentication method(s) that the client may use.
         */
        private Set<String> clientAuthenticationMethods = new HashSet<>();

        /**
         * Authorization grant type(s) that the client may use.
         */
        private Set<String> authorizationGrantTypes = new HashSet<>();

        /**
         * Redirect URI(s) that the client may use in redirect-based flows.
         */
        private Set<String> redirectUris = new HashSet<>();

        /**
         * Scope(s) that the client may use.
         */
        private Set<String> scopes = new HashSet<>();
    }

    /**
     * Token settings of the registered client.
     */
    @Data
    public static class Token {

        /**
         * Time-to-live for an authorization code.
         */
        private Duration authorizationCodeTimeToLive;

        /**
         * Time-to-live for an access token.
         */
        private Duration accessTokenTimeToLive;

        /**
         * Token format for an access token.
         */
        private String accessTokenFormat;

        /**
         * Whether refresh tokens are reused or a new refresh token is issued when
         * returning the access token response.
         */
        private boolean reuseRefreshTokens;

        /**
         * Time-to-live for a refresh token.
         */
        private Duration refreshTokenTimeToLive;

        /**
         * JWS algorithm for signing the ID Token.
         */
        private String idTokenSignatureAlgorithm;
    }
}
