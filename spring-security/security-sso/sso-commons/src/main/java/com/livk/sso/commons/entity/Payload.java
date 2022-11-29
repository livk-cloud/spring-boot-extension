package com.livk.sso.commons.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Payload
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payload {

    private String id;

    private User userInfo;

}
