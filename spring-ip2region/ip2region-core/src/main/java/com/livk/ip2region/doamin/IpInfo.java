package com.livk.ip2region.doamin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * IpInfo
 * </p>
 *
 * @author livk
 * @date 2022/8/18
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

    public static IpInfo of(String info) {
        return new IpInfo(info);
    }

    private String checkData(String data) {
        return "0".equals(data) ? null : data;
    }
}
