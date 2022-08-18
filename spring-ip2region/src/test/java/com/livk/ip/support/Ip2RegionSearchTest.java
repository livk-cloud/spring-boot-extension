package com.livk.ip.support;

import com.livk.ip.entity.IpInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * Ip2RegionSearchTest
 * </p>
 *
 * @author livk
 * @date 2022/8/18
 */
@SpringBootTest("ip2region.enabled=true")
class Ip2RegionSearchTest {

    @Autowired
    private Ip2RegionSearch ip2RegionSearch;

    @Test
    void searchAsString() {
        String result = ip2RegionSearch.searchAsString("110.242.68.66");
        String str = "中国|0|河北省|保定市|联通";
        assertEquals(result, str);
    }

    @Test
    void searchAsInfo() {
        IpInfo result = ip2RegionSearch.searchAsInfo("110.242.68.66");
        IpInfo ipInfo = new IpInfo().setNation("中国").setProvince("河北省").setCity("保定市").setOperator("联通");
        assertEquals(result, ipInfo);
    }

    @Test
    void searchAsJson() {
        String result = ip2RegionSearch.searchAsJson("110.242.68.66");
        String json = "{\"nation\":\"中国\",\"area\":null,\"province\":\"河北省\",\"city\":\"保定市\",\"operator\":\"联通\"}";
        assertEquals(result, json);
    }
}

