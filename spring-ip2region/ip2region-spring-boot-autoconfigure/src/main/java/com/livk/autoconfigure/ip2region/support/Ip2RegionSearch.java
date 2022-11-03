package com.livk.autoconfigure.ip2region.support;

import com.livk.autoconfigure.ip2region.doamin.IpInfo;
import com.livk.ip2region.core.Searcher;
import com.livk.util.JacksonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Ip2RegionSearch
 * </p>
 *
 * @author livk
 * @date 2022/8/18
 */
@Slf4j
@RequiredArgsConstructor
public class Ip2RegionSearch {

    private final Searcher searcher;

    public String searchAsString(String ip) {
        try {
            return searcher.search(ip);
        } catch (Exception e) {
            log.error("Ip2Region Searcher fail! IP:{}", ip);
            return "";
        }
    }

    public IpInfo searchAsInfo(String ip) {
        return IpInfo.of(this.searchAsString(ip));
    }

    public String searchAsJson(String ip) {
        return JacksonUtils.toJsonStr(this.searchAsInfo(ip));
    }
}
