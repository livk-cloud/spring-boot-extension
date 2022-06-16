package com.livk.ck.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 * @date 2022/4/24
 */
@Data
@Accessors(chain = true)
public class User {

	private Long id;

	private String appId;

	private String version;

	private Date regTime;

}
