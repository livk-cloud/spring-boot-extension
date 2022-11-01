package com.livk.ip.controller;

import com.livk.common.Pair;
import com.livk.ip.annotation.IP;
import com.livk.ip.annotation.RequestIp;
import com.livk.ip.entity.IpInfo;
import com.livk.ip.support.Ip2RegionSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Ip2RegionController
 * </p>
 *
 * @author livk
 * @date 2022/8/19
 */
@Validated
@RestController
@RequestMapping("ip")
@RequiredArgsConstructor
public class Ip2RegionController {

    private final Ip2RegionSearch search;

    @GetMapping
    public HttpEntity<IpInfo> get(@IP String ip) {
        return ResponseEntity.ok(search.searchAsInfo(ip));
    }

    @PostMapping
    public HttpEntity<Pair<String, IpInfo>> post(@RequestIp String ip) {
        return ResponseEntity.ok(Pair.of(ip, search.searchAsInfo(ip)));
    }
}
