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

package com.livk.autoconfigure.ip2region.doamin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * IpInfo
 * </p>
 *
 * @author livk
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class IpInfo {

    private String nation;

    private String area;

    private String province;

    private String city;

    private String operator;

    private IpInfo(String info) {
        String[] arr = info.split("\\|");
        nation = checkData(arr[0]);
        area = checkData(arr[1]);
        province = checkData(arr[2]);
        city = checkData(arr[3]);
        operator = checkData(arr[4]);
    }

    /**
     * Of ip info.
     *
     * @param info the info
     * @return the ip info
     */
    public static IpInfo of(String info) {
        return new IpInfo(info);
    }

    private String checkData(String data) {
        return "0".equals(data) ? null : data;
    }
}
