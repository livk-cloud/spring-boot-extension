package com.livk.auth.server.autoconfigure;

import com.livk.auth.server.autoconfigure.OAuth2AuthorizationServerProperties.Client;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * @author livk
 */
final class OAuth2AuthorizationServerPropertiesMapper {

    private final OAuth2AuthorizationServerProperties properties;

    OAuth2AuthorizationServerPropertiesMapper(OAuth2AuthorizationServerProperties properties) {
        this.properties = properties;
    }

    AuthorizationServerSettings asAuthorizationServerSettings() {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        OAuth2AuthorizationServerProperties.Endpoint endpoint = this.properties.getEndpoint();
        OAuth2AuthorizationServerProperties.OidcEndpoint oidc = endpoint.getOidc();
        AuthorizationServerSettings.Builder builder = AuthorizationServerSettings.builder();
        map.from(this.properties::getIssuer).to(builder::issuer);
        map.from(endpoint::getAuthorizationUri).to(builder::authorizationEndpoint);
        map.from(endpoint::getTokenUri).to(builder::tokenEndpoint);
        map.from(endpoint::getJwkSetUri).to(builder::jwkSetEndpoint);
        map.from(endpoint::getTokenRevocationUri).to(builder::tokenRevocationEndpoint);
        map.from(endpoint::getTokenIntrospectionUri).to(builder::tokenIntrospectionEndpoint);
        map.from(oidc::getClientRegistrationUri).to(builder::oidcClientRegistrationEndpoint);
        map.from(oidc::getUserInfoUri).to(builder::oidcUserInfoEndpoint);
        return builder.build();
    }

    List<RegisteredClient> asRegisteredClients() {
        List<RegisteredClient> registeredClients = new ArrayList<>();
        this.properties.getClient()
                .forEach((registrationId, client) -> registeredClients.add(getRegisteredClient(registrationId, client)));
        return registeredClients;
    }

    private RegisteredClient getRegisteredClient(String registrationId, Client client) {
        OAuth2AuthorizationServerProperties.Registration registration = client.getRegistration();
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        RegisteredClient.Builder builder = RegisteredClient.withId(registrationId);
        map.from(registration::getClientId).to(builder::clientId);
        map.from(registration::getClientSecret).to(builder::clientSecret);
        map.from(registration::getClientName).to(builder::clientName);
        registration.getClientAuthenticationMethods()
                .forEach((clientAuthenticationMethod) -> map.from(clientAuthenticationMethod)
                        .as(ClientAuthenticationMethod::new)
                        .to(builder::clientAuthenticationMethod));
        registration.getAuthorizationGrantTypes()
                .forEach((authorizationGrantType) -> map.from(authorizationGrantType)
                        .as(AuthorizationGrantType::new)
                        .to(builder::authorizationGrantType));
        registration.getRedirectUris().forEach((redirectUri) -> map.from(redirectUri).to(builder::redirectUri));
        registration.getScopes().forEach((scope) -> map.from(scope).to(builder::scope));
        builder.clientSettings(getClientSettings(client, map));
        builder.tokenSettings(getTokenSettings(client, map));
        return builder.build();
    }

    private ClientSettings getClientSettings(Client client, PropertyMapper map) {
        ClientSettings.Builder builder = ClientSettings.builder();
        map.from(client::isRequireProofKey).to(builder::requireProofKey);
        map.from(client::isRequireAuthorizationConsent).to(builder::requireAuthorizationConsent);
        map.from(client::getJwkSetUri).to(builder::jwkSetUrl);
        map.from(client::getTokenEndpointAuthenticationSigningAlgorithm)
                .as(this::jwsAlgorithm)
                .to(builder::tokenEndpointAuthenticationSigningAlgorithm);
        return builder.build();
    }

    private TokenSettings getTokenSettings(Client client, PropertyMapper map) {
        OAuth2AuthorizationServerProperties.Token token = client.getToken();
        TokenSettings.Builder builder = TokenSettings.builder();
        map.from(token::getAuthorizationCodeTimeToLive).to(builder::authorizationCodeTimeToLive);
        map.from(token::getAccessTokenTimeToLive).to(builder::accessTokenTimeToLive);
        map.from(token::getAccessTokenFormat).as(OAuth2TokenFormat::new).to(builder::accessTokenFormat);
        map.from(token::isReuseRefreshTokens).to(builder::reuseRefreshTokens);
        map.from(token::getRefreshTokenTimeToLive).to(builder::refreshTokenTimeToLive);
        map.from(token::getIdTokenSignatureAlgorithm)
                .as(this::signatureAlgorithm)
                .to(builder::idTokenSignatureAlgorithm);
        return builder.build();
    }

    private JwsAlgorithm jwsAlgorithm(String signingAlgorithm) {
        String name = signingAlgorithm.toUpperCase();
        JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.from(name);
        if (jwsAlgorithm == null) {
            jwsAlgorithm = MacAlgorithm.from(name);
        }
        return jwsAlgorithm;
    }

    private SignatureAlgorithm signatureAlgorithm(String signatureAlgorithm) {
        return SignatureAlgorithm.from(signatureAlgorithm.toUpperCase());
    }

}

