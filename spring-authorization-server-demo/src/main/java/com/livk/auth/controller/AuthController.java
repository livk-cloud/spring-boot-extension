package com.livk.auth.controller;

import com.livk.auth.domain.Users;
import com.nimbusds.jose.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * AuthController
 * </p>
 *
 * @author livk
 * @date 2021/12/22
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RestTemplate restTemplate;
    private final ProviderSettings providerSettings;

    /**
     * token：XXXX
     *
     * @param code 校验码
     * @return token
     */
    @GetMapping("/oauth2/livk/code/livk-client")
    public HttpEntity<String> oAuth(@RequestParam String code) {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Users user) {
            log.info("User:{}", user);
        }
        var basic = Base64.encode("livk-client:secret").toString();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(basic);
        var param = new LinkedMultiValueMap<String,String>();
        param.add("grant_type", "authorization_code");
        param.add("code", code);
        param.add("redirect_uri", "http://127.0.0.1:9000/oauth2/livk/code/livk-client");
        var requestEntity = new HttpEntity<>(param, headers);
        var result = restTemplate.postForObject(providerSettings.getIssuer() + providerSettings.getTokenEndpoint(), requestEntity, String.class);
        return ResponseEntity.ok(result);
    }
}
