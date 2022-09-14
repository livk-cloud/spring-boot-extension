package com.livk.retry.controller;

import com.livk.retry.support.RemoteSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * RetryController
 * </p>
 *
 * @author livk
 * @date 2022/4/29
 */
@RestController
@RequiredArgsConstructor
public class RetryController {

    private final RemoteSupport remoteSupport;

    @GetMapping("/retry")
    public HttpEntity<String> retry() {
        return ResponseEntity.ok(remoteSupport.call("fail"));
    }

}
