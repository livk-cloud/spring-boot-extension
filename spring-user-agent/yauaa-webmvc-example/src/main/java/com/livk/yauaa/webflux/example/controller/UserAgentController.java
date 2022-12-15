package com.livk.yauaa.webflux.example.controller;

import com.livk.autoconfigure.useragent.annotation.UserAgentInfo;
import com.livk.autoconfigure.useragent.yauaa.support.UserAgentContextHolder;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * UserAgentController
 * </p>
 *
 * @author livk
 */
@RestController
@RequestMapping("user-agent")
public class UserAgentController {

    @GetMapping
    public HttpEntity<Map<String, Map<String, String>>> get(@UserAgentInfo UserAgent userAgent) {
        Map<String, Map<String, String>> map = Map.of(UUID.randomUUID().toString(), convert(userAgent),
                UUID.randomUUID().toString(), convert(UserAgentContextHolder.get()));
        return ResponseEntity.ok(map);
    }

    private Map<String, String> convert(UserAgent userAgent) {
        return userAgent.getAvailableFieldNamesSorted()
                .stream()
                .collect(Collectors.toMap(Function.identity(), userAgent::getValue));
    }
}
