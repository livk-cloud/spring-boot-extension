package com.livk.browscap.mvc.example.controller;

import com.blueconic.browscap.Capabilities;
import com.livk.autoconfigure.useragent.annotation.UserAgentInfo;
import com.livk.autoconfigure.useragent.servlet.UserAgentContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

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
    public HttpEntity<Map<String, Capabilities>> get(@UserAgentInfo Capabilities capabilities) {
        Map<String, Capabilities> map = Map.of(UUID.randomUUID().toString(), capabilities,
                UUID.randomUUID().toString(), (Capabilities) UserAgentContextHolder.getUserAgentContext().obj());
        return ResponseEntity.ok(map);
    }
}
