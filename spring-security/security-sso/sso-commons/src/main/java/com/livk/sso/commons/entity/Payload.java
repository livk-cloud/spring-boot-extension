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
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payload {

    private String id;

    private User userInfo;

}
