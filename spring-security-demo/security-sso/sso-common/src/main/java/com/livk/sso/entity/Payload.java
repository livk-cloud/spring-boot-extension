package com.livk.sso.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * Payload
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@Data
@Builder
public class Payload<T> {

    private String id;

    private T userInfo;

    private Date expiration;
}
