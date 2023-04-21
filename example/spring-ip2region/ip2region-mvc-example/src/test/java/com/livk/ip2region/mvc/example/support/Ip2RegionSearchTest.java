/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.ip2region.mvc.example.support;

import com.livk.autoconfigure.ip2region.doamin.IpInfo;
import com.livk.autoconfigure.ip2region.support.Ip2RegionSearch;
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

