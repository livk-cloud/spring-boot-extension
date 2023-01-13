package com.livk.autoconfigure.ip2region.support;

import com.livk.autoconfigure.ip2region.doamin.IpInfo;
import com.livk.commons.jackson.JacksonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

/**
 * <p>
 * Ip2RegionSearch
 * </p>
 *
 * @author livk
 */
@Slf4j
@RequiredArgsConstructor
public class Ip2RegionSearch {

    private final Searcher searcher;

    /**
     * Search as string.
     *
     * @param ip the ip
     * @return the string
     */
    public String searchAsString(String ip) {
        try {
            return searcher.search(ip);
        } catch (Exception e) {
            log.error("Ip2Region Searcher fail! IP:{}", ip);
            return "";
        }
    }

    /**
     * Search as info ip info.
     *
     * @param ip the ip
     * @return the ip info
     */
    public IpInfo searchAsInfo(String ip) {
        return IpInfo.of(this.searchAsString(ip));
    }

    /**
     * Search as json string.
     *
     * @param ip the ip
     * @return the string
     */
    public String searchAsJson(String ip) {
        return JacksonUtils.toJsonStr(this.searchAsInfo(ip));
    }
}
